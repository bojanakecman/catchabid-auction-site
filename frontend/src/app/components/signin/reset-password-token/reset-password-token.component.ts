import { Component, OnInit, Input } from '@angular/core';
import { ResetPasswordComponent } from '../reset-password/reset-password.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { PasswordManagementService } from '../../../services/password-management.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-reset-password-token',
  templateUrl: './reset-password-token.component.html',
  styleUrls: ['./reset-password-token.component.scss']
})
export class ResetPasswordTokenComponent implements OnInit {

  token: Number;

  @Input() 
  public email: String;

  constructor(
    private modalService: NgbModal,
    private passwordManagementService: PasswordManagementService,
    private toast: ToastrService
  ) { }

  ngOnInit(): void {
  }

  onModalClose(){
    this,this.modalService.dismissAll();
  }

  sendResetPasswordToken(){
    this.passwordManagementService.sendResetPasswordToken(this.email, this.token)
    .subscribe(
      data => {
        this.openResetPasswordModal();
      },
      error => {
        this.toast.error(error.error.message);
    });
    
  }

  openResetPasswordModal(){
    this.modalService.dismissAll();
    const modal = this.modalService.open(ResetPasswordComponent);
    modal.componentInstance.email = this.email;
  }


}
