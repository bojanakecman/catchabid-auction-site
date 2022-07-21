import { Component, OnInit } from '@angular/core';
import {PasswordManagementService} from "../../../services/password-management.service";
import {ToastrService} from "ngx-toastr";
import {NgbdModalConfirm} from "../profile.component";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'update-password',
  templateUrl: './update-password.component.html',
  styleUrls: ['./update-password.component.css']
})
export class UpdatePasswordComponent implements OnInit {
  token: Number;
  showRequestToken:boolean=true;
  showApproveToken:boolean=false;
  showPasswordChange:boolean=false;
  showChangeSuccess:boolean=false;
  tokenRequested:boolean
  password:String;

  constructor(    private passwordManagementService: PasswordManagementService,
                  private toast: ToastrService,
                  private modalService: NgbModal,) { }

  ngOnInit(): void {
    this.openPane();
    this.showRequestToken=true;
    const togglePassword = document.querySelector('#togglePassword');
    const toggleConfirmPassword = document.querySelector('#toggleConfirmPassword');
    const password = document.querySelector('#password');
    const confirmPassword = document.querySelector('#confirmPassword');

    togglePassword.addEventListener('click', function (e) {
      // toggle the type attribute
      const type = password.getAttribute('type') === 'password' ? 'text' : 'password';
      password.setAttribute('type', type);
      // toggle the eye slash icon
      this.classList.toggle('fa-eye-slash');
    });

    toggleConfirmPassword.addEventListener('click', function (e) {
      // toggle the type attribute
      const type = confirmPassword.getAttribute('type') === 'password' ? 'text' : 'password';
      confirmPassword.setAttribute('type', type);
      // toggle the eye slash icon
      this.classList.toggle('fa-eye-slash');
    });
  }

  tokenRequest(){
    this.showRequestToken=false;
    this.showApproveToken=true;
    console.log("token value ", this.tokenRequested)
    this.passwordManagementService.requestPasswordReset(localStorage.email)
      .subscribe(
        data => {
        },
        error => {
          this.toast.error(error.error.message);
        });

  }

  approveToken(){
    this.showApproveToken=false;
    this.showPasswordChange=true;
    this.passwordManagementService.sendResetPasswordToken(localStorage.email, this.token)
      .subscribe(
        data => {
        console.log("token approved")
        },
        error => {
          this.toast.error(error.error.message);
        });

  }
  changePassword(){
    this.passwordManagementService.resetPassword(localStorage.email, this.password)
      .subscribe(
        data => {
          this.toast.success("Password successfully changed");
          console.log("Password successfully changed");
          this.showPasswordChange=false;
          this.showChangeSuccess=true;
        },
        error => {
          this.toast.error(error.error.message);
        });

  }
  openPane(){
    this.showChangeSuccess=false;
    this.showPasswordChange=false;
    this.showRequestToken=true;
    this.showApproveToken=false
  }
}
