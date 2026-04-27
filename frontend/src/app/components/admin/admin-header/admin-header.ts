import { Component, Input} from '@angular/core';



@Component({
  selector: 'app-admin-header',
  standalone: false,
  templateUrl: './admin-header.html',
  styleUrl: './admin-header.css',
})
export class AdminHeader {
@Input() title: string = 'Titre section';
}
