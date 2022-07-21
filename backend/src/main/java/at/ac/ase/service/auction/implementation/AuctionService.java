package at.ac.ase.service.auction.implementation;

import at.ac.ase.controllers.NotificationWebSocketController;
import at.ac.ase.dto.AuctionCreationDTO;
import at.ac.ase.dto.AuctionPostSendDTO;
import at.ac.ase.dto.AuctionQueryDTO;
import at.ac.ase.dto.ContactFormDTO;
import at.ac.ase.dto.translator.AuctionDtoTranslator;
import at.ac.ase.entities.*;
import at.ac.ase.repository.auction.AuctionPostQuery;
import at.ac.ase.repository.auction.AuctionRepository;
import at.ac.ase.service.auction.IAuctionService;
import at.ac.ase.service.twitter.ITwitterService;
import at.ac.ase.service.notification.INotificationService;
import at.ac.ase.service.user.IAuctionHouseService;
import at.ac.ase.service.user.IRegularUserService;
import at.ac.ase.util.AuctionTweetJob;
import at.ac.ase.util.exceptions.*;
import com.lowagie.text.DocumentException;
import at.ac.ase.util.AuctionFinishedNotificationJob;
import at.ac.ase.util.AuctionStartedNotificationJob;
import at.ac.ase.util.exceptions.ObjectNotFoundException;
import at.ac.ase.util.exceptions.WrongSubscriberException;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.LockModeType;
import javax.validation.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuctionService implements IAuctionService {
    private static final Logger logger = LoggerFactory.getLogger(AuctionService.class);

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private IRegularUserService regularUserService;

    @Autowired
    private IAuctionHouseService auctionHouseService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuctionDtoTranslator auctionDtoTranslator;

    @Autowired
    JavaMailSender emailSender;

    @Autowired
    IRegularUserService userService;

    @Autowired
    INotificationService notificationService;

    @Autowired
    NotificationWebSocketController webSocketController;

    @Autowired
    ITwitterService twitterService;

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @PostConstruct
    private void scheduleJobForAuctions() {
        logger.info("Scheduling jobs for stored auctions");
        List<AuctionPost> auctionPostList = auctionRepository.findAll();
        for(AuctionPost auctionPost: auctionPostList){
            scheduleNotificationJob(auctionPost);
        }

    }
    private final int POPULAR_AUCTION_SUBSCRIPTIONS_MINIMUM = 1;
    private final int AWESOME_AUCTION_SUBSCRIPTIONS_MINIMUM = 2;

    @Override
    public Optional<AuctionPost> getAuctionPost(Long id) {
        return auctionRepository.findById(id);
    }

    @Override
    public AuctionPost saveAuction(AuctionPost auctionPost) {
        AuctionPost auctionPostCreated = auctionRepository.save(auctionPost);
        if(auctionPost.getId() == null){
            scheduleNotificationJob(auctionPostCreated);
        }
        return auctionPostCreated;
    }

    @Override
    @Transactional
    public AuctionPostSendDTO cancelAuction(User user, Long auctionPostId) {
        AuctionPost auction = auctionRepository.findById(auctionPostId)
                .orElseThrow(ObjectNotFoundException::new);

        if(!auction.getCreator().getId().equals(user.getId())) {
            throw new AuthorizationException();
        }
        if(auction.isUpcoming()) {
            auction.setStatus(Status.CANCELLED);
        }
        else {
            throw new AuctionCancellationException();
        }
        return auctionDtoTranslator.toSendDto(auction, true);
    }

    @Override
    public AuctionPost toAuctionPostEntity(User user, AuctionCreationDTO auctionPostDTO) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        AuctionPost auctionPost = modelMapper.map(auctionPostDTO, AuctionPost.class);
        if (auctionPostDTO.getId() != null) {
            auctionPost = auctionRepository
                    .findById(auctionPost.getId()).orElseThrow(ObjectNotFoundException::new);
        } else {
            auctionPost.setStatus(Status.UPCOMING);
        }
        auctionPost.setName(auctionPostDTO.getName());
        auctionPost.setCategory(auctionPostDTO.getCategory());
        auctionPost.setStartTime(auctionPostDTO.getStartTime());
        auctionPost.setEndTime(auctionPostDTO.getEndTime());
        auctionPost.setMinPrice(auctionPostDTO.getMinPrice());
        auctionPost.setDescription(auctionPostDTO.getDescription());
        auctionPost.setCreator(user);
        auctionPost.setImage(Base64.getDecoder().decode(auctionPostDTO.getImage()));
        auctionPost.setAddress(new Address(auctionPostDTO.getCountry(), auctionPostDTO.getCity(),
                auctionPostDTO.getAddress(), auctionPostDTO.getHouseNr()));
        return auctionPost;
    }

    @Override
    public AuctionPost toAuctionPostEntity( AuctionPostSendDTO auctionPostDTO) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        AuctionPost auctionPost = modelMapper.map(auctionPostDTO, AuctionPost.class);
        if (auctionPostDTO.getId() != null) {
            auctionPost = auctionRepository
                    .findById(auctionPost.getId()).orElseThrow(ObjectNotFoundException::new);
        } else {
            auctionPost.setStatus(Status.UPCOMING);
        }
        auctionPost.setName(auctionPostDTO.getAuctionName());
        auctionPost.setCategory(Category.valueOf(auctionPostDTO.getCategory()));
        auctionPost.setStartTime(auctionPostDTO.getStartTime());
        auctionPost.setEndTime(auctionPostDTO.getEndTime());
        auctionPost.setMinPrice(auctionPostDTO.getMinPrice());
        auctionPost.setDescription(auctionPostDTO.getDescription());
        Optional<AuctionHouse> auctionHouse = auctionHouseService.getAuctionHouseById(auctionPostDTO.getCreatorId());
        if (auctionHouse.isPresent()){
            auctionPost.setCreator(auctionHouse.get());
        }else{
            Optional<RegularUser> regularUser = regularUserService.getUserById(auctionPostDTO.getCreatorId());
            if (regularUser.isPresent()){
                auctionPost.setCreator(regularUser.get());
            }else{
                if (auctionPost.getCreator()==null){
                    //ERROR Object incomplete
                }
            }
        }
        auctionPost.setImage(Base64.getDecoder().decode(auctionPostDTO.getImage()));
        auctionPost.setAddress(new Address(auctionPostDTO.getCountry(), auctionPostDTO.getCity(),
                auctionPostDTO.getAddress(), auctionPostDTO.getHouseNr()));
        return auctionPost;
    }

    @Override
    public Category[] getCategories() {
        Category[] categories = Category.values();
        return categories;
    }

    @Override
    public List<String> getCountriesWhereAuctionsExist() {
        return auctionRepository.getAllCountriesWhereAuctionsExist();
    }


    @Override
    public List<AuctionPost> getAllAuctions() {
        return auctionRepository.findAll();
    }

    @Override
    public List<AuctionPostSendDTO> getAllAuctions(Integer auctionsPerPage, Integer pageNr) {
        Page<AuctionPost> result = auctionRepository.findAll(getPageForFutureAuctions(auctionsPerPage, pageNr, Sort.by("startTime").ascending()));
        return convertAuctionsToDTO(result.getContent());
    }

    @Override
    public List<AuctionPostSendDTO> searchAuctions(AuctionQueryDTO query) {
        AuctionPostQuery entityQuery = auctionDtoTranslator.toEntity(query);
        if (entityQuery.isEmptySearch()) {
            entityQuery.setCategories(getPreferences(entityQuery.getUserEmail(), entityQuery.isUseUserPreferences()));
        }
        List<AuctionPost> foundAuctions = auctionRepository.query(entityQuery);
        logger.info("Size of auctions: " + foundAuctions.size());
        return auctionDtoTranslator.toDtoList(foundAuctions);
    }

    private List<AuctionPostSendDTO> convertAuctionsToDTO(Collection<AuctionPost> auctions) {
        List<AuctionPostSendDTO> auctionPostSendDTOS = new ArrayList<>();
        auctions.forEach((x) -> auctionPostSendDTOS.add(auctionDtoTranslator.toSendDto(x, true)));
        return auctionPostSendDTOS;
    }

    private Pageable getPageForFutureAuctions(Integer auctionsPerPage, Integer pageNr, Sort sortMethod) {
        if (pageNr == null || pageNr < 0) {
            pageNr = 0;
        }
        if (auctionsPerPage == null || auctionsPerPage < 1) {
            auctionsPerPage = 50;
        }
        return PageRequest.of(pageNr, auctionsPerPage, sortMethod);
    }

    private List<Category> getPreferences(String userEmail, boolean usePreferences) {
        RegularUser regularUser = regularUserService.getUserByEmail(userEmail);
        if (regularUser != null) {
            if (usePreferences) {
                return regularUser.getPreferences().stream().collect(Collectors.toList());
            } else {
                return Arrays.stream(getCategories()).filter(e -> !regularUser.getPreferences().contains(e)).collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }

    public AuctionPostSendDTO postContactForm(Long auctionId, User user, ContactForm contactForm) {
        AuctionPost auction = auctionRepository.findById(auctionId)
                .orElseThrow(ObjectNotFoundException::new);

        if (!auction.getHighestBid().getUser().getId().equals(user.getId())) {
            throw new AuthorizationException();
        }
        Set<ConstraintViolation<ContactForm>> violations = validator.validate(contactForm);

        if (violations.isEmpty()) {
            auction.setContactForm(contactForm);
            return auctionDtoTranslator.toSendDto(saveAuction(auction), true);
        } else {
            for (ConstraintViolation<ContactForm> violation : violations) {
                throw new ValidationException(violation.getMessage());
            }
            return null;
        }
    }

    public ContactFormDTO getContactForm(Long auctionPostId, User user) {
        AuctionPost auction = auctionRepository.findById(auctionPostId)
                .orElseThrow(ObjectNotFoundException::new);

        if (!auction.getCreator().getId().equals(user.getId())) {
            throw new AuthorizationException();
        }

        return convertDTOToContactForm(auction.getContactForm(), auctionPostId);
    }

    private ContactFormDTO convertDTOToContactForm(ContactForm contactForm, Long auctionPostId) {
        ContactFormDTO contactFormDTO = modelMapper.map(contactForm, ContactFormDTO.class);
        contactFormDTO.setCountry(contactForm.getAddress().getCountry());
        contactFormDTO.setCity(contactForm.getAddress().getCity());
        contactFormDTO.setStreet(contactForm.getAddress().getStreet());
        contactFormDTO.setHouseNr(contactForm.getAddress().getHouseNr());
        contactFormDTO.setAuctionPostId(auctionPostId);
        return contactFormDTO;
    }

    public ContactForm convertContactFormToDTO(ContactFormDTO contactFormDTO) {
        ContactForm contactForm = modelMapper.map(contactFormDTO, ContactForm.class);
        contactForm.setAddress(new Address(contactFormDTO.getCountry(), contactFormDTO.getCity(), contactFormDTO.getStreet(), contactFormDTO.getHouseNr()));
        return contactForm;
    }

    @Override
    public boolean isAuctionPayable(AuctionPost auctionpost) {
        return Objects.nonNull(auctionpost.getHighestBid()) &&
            auctionpost.getEndTime().isBefore(LocalDateTime.now(ZoneOffset.UTC));
    }

    @Override
    public List<AuctionPost> getAllWonAuctionPostsForUser(User user) {
        return auctionRepository.findAllByHighestBidUserIdAndEndTimeLessThan(
            user.getId(), LocalDateTime.now(ZoneOffset.UTC));
    }

    @Override
    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public AuctionPost subscribeToAuction(AuctionPost auctionPost, User user) {
        if(auctionPost.getCreator().getId().equals(user.getId())) {
            throw new WrongSubscriberException();
        }
        if(auctionPost.getStatus().equals(Status.CANCELLED)){
            throw new AuctionCancelledException();
        }
        //ToDo: check if user trying to subsribe is a regaular user and not an auction house (also in frontend)
        Set<RegularUser> subscriptions = auctionPost.getSubscriptions();
        subscriptions.add((RegularUser) user);
        auctionPost.setSubscriptions(subscriptions);

        AuctionPost updatedAuctionPost = auctionRepository.save(auctionPost);
        scheduleNotificationJob(updatedAuctionPost);

        adaptAuctionPopularity(updatedAuctionPost);

        return updatedAuctionPost;
    }

    @Override
    public AuctionPost unsubscribeFromAuction(AuctionPost auctionPost, User user) {
        Set<RegularUser> subscriptions = auctionPost.getSubscriptions();
        subscriptions.removeIf(subscribedUser -> subscribedUser.getId().equals(user.getId()));
        auctionPost.setSubscriptions(subscriptions);
        AuctionPost updatedAuctionPost = auctionRepository.save(auctionPost);
        scheduleNotificationJob(updatedAuctionPost);
        return updatedAuctionPost;
    }

    public List<AuctionPostSendDTO> getMyAuctions(User user){
        List<AuctionPost> retrieved = auctionRepository.findALlByCreatorId(user.getId());
        logger.info("Retrieved these posts " + retrieved );
        return auctionDtoTranslator.toDtoList(retrieved);
    }

    public Set<AuctionPostSendDTO> getMySubscriptions(RegularUser user){
        Set<AuctionPost> retrieved = userService.getUserByEmail(user.getEmail()).getSubscriptions();
                logger.info("subs"+retrieved);
        return auctionDtoTranslator.toDtoSet(retrieved);
    }

    @Override
    public AuctionPost sendConfirmation(AuctionPost auctionPost, User user) throws IOException, DocumentException {
       generatePdfFromHtml(parseThymeleafTemplate(auctionPost));
       sendConfirmationEmail(auctionPost.getContactForm());
       return null;
    }

    private String parseThymeleafTemplate(AuctionPost auctionPost) throws IOException {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateString = formatDate.format(auctionPost.getEndTime());

        Context context = new Context();
        context.setVariable("auction", auctionPost);
        context.setVariable("created", dateString);
        if (auctionPost.getCreator() instanceof RegularUser) {
            context.setVariable("user", "regularUser");
        } else {
            context.setVariable("user", "auctionHouse");
        }

        return templateEngine.process("confirmation", context);
    }

    private void generatePdfFromHtml(String html) throws IOException, DocumentException {
        String outputFolder = System.getProperty("user.home") + File.separator + "thymeleaf2.pdf";
        OutputStream outputStream = new FileOutputStream(outputFolder);

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(outputStream);

        outputStream.close();
    }

    private void sendConfirmationEmail(ContactForm contactForm){
        try {
            MimeMessage message = emailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("noreply.catchabid@gmail.com");
            helper.setTo(contactForm.getEmail());
            helper.setSubject("Confirmation about won auction");
            helper.setText("Dear " + contactForm.getFirstName() + " " + contactForm.getLastName() +
                    ", <br/> congratulations on winning an auction. " +
                    "You can find the official confirmation about the won auction in the attachment. <br/>", true);

            FileSystemResource file = new FileSystemResource(new File(System.getProperty("user.home") + File.separator + "thymeleaf2.pdf"));
            helper.addAttachment("confirmation.pdf", file);

            emailSender.send(message);
        } catch (MessagingException e) {
            throw new EmailNotSentException();
        }
    }

    public void scheduleNotificationJob(AuctionPost auctionPost){
        try {
            SchedulerFactory schedFact = new StdSchedulerFactory();
            Scheduler sched = schedFact.getScheduler();
            String jobName = auctionPost.getId().toString();
            String triggerName = "trigger-" + auctionPost.getId();
            ZoneId zone = ZoneId.of("UTC");
            Trigger trigger = null;
            JobDetail job = null;

            if(auctionPost.getStartTime().isAfter(LocalDateTime.now(ZoneOffset.UTC))) {
                job = JobBuilder.newJob(AuctionStartedNotificationJob.class)
                        .withIdentity(jobName, "group1")
                        .build();

                sched.deleteJob(job.getKey());

                job.getJobDataMap().put("auctionPost", auctionPost);
                job.getJobDataMap().put("emailSender", emailSender);
                job.getJobDataMap().put("notificationService", notificationService);
                job.getJobDataMap().put("auctionService", this);
                job.getJobDataMap().put("webSocketController", webSocketController);



                trigger = TriggerBuilder.newTrigger()
                        .withIdentity(triggerName, "group1")
                        .startAt(java.util.Date
                                .from(auctionPost.getStartTime().atZone(zone)
                                        .toInstant()))
                        .forJob(jobName, "group1")
                        .build();
            }else{
                if(auctionPost.getEndTime().isAfter(LocalDateTime.now(ZoneOffset.UTC))) {
                    job = JobBuilder.newJob(AuctionFinishedNotificationJob.class)
                            .withIdentity(jobName, "group1")
                            .build();

                    sched.deleteJob(job.getKey());

                    job.getJobDataMap().put("auctionPost", auctionPost);
                    job.getJobDataMap().put("emailSender", emailSender);
                    job.getJobDataMap().put("notificationService", notificationService);
                    job.getJobDataMap().put("auctionService", this);
                    job.getJobDataMap().put("webSocketController", webSocketController);

                    trigger = TriggerBuilder.newTrigger()
                            .withIdentity(triggerName, "group1")
                            .startAt(java.util.Date
                                    .from(auctionPost.getEndTime().atZone(zone)
                                            .toInstant()))
                            .forJob(jobName, "group1")
                            .build();
                }
            }

            if(job != null && trigger != null) {
                sched.scheduleJob(job, trigger);
                sched.start();
            }

        }catch (SchedulerException e){
            logger.error(e.getMessage());
        }
    }

    private void adaptAuctionPopularity(AuctionPost auctionPost) {

        if (auctionPost.getSubscriptions().size() >= POPULAR_AUCTION_SUBSCRIPTIONS_MINIMUM
                && AuctionPopularity.DEFAULT.equals(auctionPost.getAuctionPopularity()))
        {
            auctionPost.setAuctionPopularity(AuctionPopularity.POPULAR);
            scheduleTweet(auctionPost, auctionPost.getStartTime(), TwitterPostType.AUCTION_START);
        }

        if (auctionPost.getSubscriptions().size() >= AWESOME_AUCTION_SUBSCRIPTIONS_MINIMUM
                && AuctionPopularity.POPULAR.equals(auctionPost.getAuctionPopularity()) )
        {
            auctionPost.setAuctionPopularity(AuctionPopularity.AWESOME);
            scheduleTweet(auctionPost, auctionPost.getStartTime().minusHours(24), TwitterPostType.AUCTION_UPCOMING);
            scheduleTweet(auctionPost, auctionPost.getEndTime().minusHours(1),    TwitterPostType.AUCTION_CLOSE_TO_END);
            scheduleTweet(auctionPost, auctionPost.getEndTime(),                  TwitterPostType.AUCTION_END);
        }
    }

    private void scheduleTweet(AuctionPost auctionPost, LocalDateTime time, TwitterPostType twitterPostType) {
        if(time.isBefore(LocalDateTime.now(ZoneOffset.UTC))) {
            return;
        }
        try {
            SchedulerFactory schedFact = new StdSchedulerFactory();
            Scheduler sched = schedFact.getScheduler();
            String jobName = auctionPost.getId().toString() + twitterPostType.name();
            String triggerName = "trigger-" + auctionPost.getId() + twitterPostType.name();
            JobDetail job = JobBuilder.newJob(AuctionTweetJob.class)
                    .withIdentity(jobName, "group2")
                    .build();
            job.getJobDataMap().put("auctionPost", auctionPost);
            job.getJobDataMap().put("twitterService", twitterService);
            job.getJobDataMap().put("twitterPostType", twitterPostType);

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerName, "group2")
                    .startAt(java.util.Date
                            .from(time.atZone(ZoneId.of("UTC"))
                                    .toInstant()))
                    .forJob(jobName, "group2")
                    .build();

            sched.scheduleJob(job, trigger);
            sched.start();

        }catch (SchedulerException e){
            logger.error(e.getMessage());
        }
    }

}