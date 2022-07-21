import { Component, OnInit, Output } from '@angular/core';
import { RegisterComponent } from '../register/register.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { SigninService } from '../../services/signin.service';
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-signin',
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.scss']
})
export class SigninComponent implements OnInit {

  email: string;
  password: string;

  constructor(
    private modalService: NgbModal,
    private signinService: SigninService,
    private router: Router,
    private toast: ToastrService,
    private userService: UserService
  ) { }

  ngOnInit(): void {
    const togglePassword = document.querySelector('#togglePassword');
    const password = document.querySelector('#password');

    togglePassword.addEventListener('click', function (e) {
      // toggle the type attribute
      const type = password.getAttribute('type') === 'password' ? 'text' : 'password';
      password.setAttribute('type', type);
      // toggle the eye slash icon
      this.classList.toggle('fa-eye-slash');
    });
  }

  onModalClose(){
    this.modalService.dismissAll();
  }

  openRegisterModal(){
    this.modalService.dismissAll();
    this.modalService.open(RegisterComponent);
  }

  openForgotPasswordModal(){
    this.modalService.dismissAll();
    this.modalService.open(ForgotPasswordComponent);
  }

  login() {
    this.signinService.login(this.email, this.password)
        .subscribe(
            data => {
              this.modalService.dismissAll();
              localStorage.setItem('token', data['token']);
              this.userService.findByEmail(this.email).subscribe(
                  user => {
                    localStorage.setItem('email', user['email']);
                    localStorage.setItem('userId', user['id'].toString());
                    if(user['firstName'] !== undefined && user['lastName'] !== undefined){
                      let userName = user['firstName'].concat(" ", user['lastName']);
                      localStorage.setItem('userName', userName);
                      window.location.reload();
                    }else{
                      localStorage.setItem('userName', user['name']);
                      window.location.reload();
                    }
                  },
                  error => this.toast.error(error.error.message)
            )},
            error => {
              this.toast.error(error.error.message);
            });
    }
}


