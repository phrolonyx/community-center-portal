import { Component, Input } from '@angular/core';

// Composant Angular dynamique avec data binding (Routing parent)
@Component({
  selector: 'app-parent-sidebar',
  standalone: false,
  templateUrl: './parent-sidebar.html',
  styleUrl: './parent-sidebar.css',
})
export class ParentSidebar {
  @Input() active: string = '';
}
