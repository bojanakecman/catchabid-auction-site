package at.ac.ase.controllers;

import at.ac.ase.entities.User;
import at.ac.ase.service.statistics.implementation.StatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class StatisticsController {
    private static final Logger logger = LoggerFactory.getLogger(StatisticsController.class);

    @Autowired
    StatisticsService service;

    @GetMapping("bidStatistics")
    public ResponseEntity<Map<String,Integer>> bidStatistics( @CurrentSecurityContext(expression = "authentication.principal")User user) throws Exception {
        logger.info("Get bid statistics for user with email " + user.getEmail());
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.getBidsStatistics(user));
        }catch (Exception e){
            logger.error("Could not retrieve bid statistics",e);
            throw new Exception("Could not retrieve bid statistics") ;
        }
    }

    @GetMapping("winStatistics")
    public ResponseEntity<Map<String,Integer>> winsStatistics( @CurrentSecurityContext(expression = "authentication.principal")User user) throws Exception {
        logger.info("Get bid statistics for user with email " + user.getEmail());
        try{
        return ResponseEntity.status(HttpStatus.OK).body(service.getWinsStatistics(user));
        }catch (Exception e){
            logger.error("Could not retrieve win statistics",e);
            throw new Exception("Could not retrieve win statistics") ;
        }
    }
    @GetMapping("winBidRatio")
    public ResponseEntity<Map<String,Double>> winBidRatio( @CurrentSecurityContext(expression = "authentication.principal")User user) throws Exception {
        logger.info("Get bid statistics for user with email " + user.getEmail());
        try{
        return ResponseEntity.status(HttpStatus.OK).body(service.getBidsWinsRatio(user));
        }catch (Exception e){
            logger.error("Could not retrieve win ratio ",e);
            throw new Exception("Could not retrieve win ratio") ;
        }
    }
    @GetMapping("myAuctionsPopularity")
    public ResponseEntity<Map<String,Integer>> popularityOfAuctions( @CurrentSecurityContext(expression = "authentication.principal")User user) throws Exception {
        logger.info("Get bid statistics for user with email " + user.getEmail());
        try{
        return ResponseEntity.status(HttpStatus.OK).body(service.getPopularityOfOwnAuctions(user));
        }catch (Exception e){
            logger.error("Could not retrieve auction popularity",e);
            throw new Exception("Could not retrieve auction popularity") ;
        }
    }
    @GetMapping("myAuctionsSuccess")
    public ResponseEntity<Map<String,Double>> successOfAuctions( @CurrentSecurityContext(expression = "authentication.principal")User user) throws Exception {
        logger.info("Get bid statistics for user with email " + user.getEmail());
        try{
        return ResponseEntity.status(HttpStatus.OK).body(service.getSuccessOfMyAuctions(user));
        }catch (Exception e){
            logger.error("Could not retrieve success of auctions",e);
            throw new Exception("Could not retrieve success of auctions") ;
        }
    }

}
