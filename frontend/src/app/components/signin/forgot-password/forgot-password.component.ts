import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ResetPasswordTokenComponent } from '../reset-password-token/reset-password-token.component';
import { PasswordManagementService } from '../../../services/password-management.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-reset-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.scss']
})
export class ForgotPasswordComponent implements OnInit {

  email: String;

  constructor(
    private modalService: NgbModal,
    private passwordManagementService: PasswordManagementService,
    private toast: ToastrService,
  ) { }

  ngOnInit(): void {
  }

  onModalClose(){
    this,this.modalService.dismissAll();
  }

  requestPasswordReset(){
    this.openResetPasswordTokenModal();
    this.toast.info("Email sent");
    this.passwordManagementService.requestPasswordReset(this.email)
    .subscribe(
      data => {
        
      },
      error => {
        this.toast.error(error.error.message);
      });
  }


  openResetPasswordTokenModal(){
    this.modalService.dismissAll();
    const modal = this.modalService.open(ResetPasswordTokenComponent);
    modal.componentInstance.email = this.email;
  }

}
