import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-animateur-header',
  standalone: false,
  templateUrl: './animateur-header.html',
  styleUrl: './animateur-header.css',
})
export class AnimateurHeader {
  @Input() title: string = 'Titre section';
}
