import { Injectable } from '@angular/core';
import {Subject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class TutorialService {

  tutorialModalClosed: Subject<void> = new Subject<void>();
  isJustRegistered: boolean;

  constructor() {
  }

}
