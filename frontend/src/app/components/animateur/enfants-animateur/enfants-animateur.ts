import {ChangeDetectionStrategy, Component, computed, OnInit, signal} from '@angular/core';
import {Child} from '../../../models/child.model';
import {ChildService} from '../../../apiServices/child-service';

@Component({
  selector: 'app-enfants-animateur',
  standalone: false,
  templateUrl: './enfants-animateur.html',
  styleUrl: './enfants-animateur.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class EnfantsAnimateur implements OnInit {

  constructor(private childService: ChildService) {}

  ngOnInit() {
    this.loadChilds()
  }

  childrens = signal<Child[]>([]);

  searchValue = signal('');

  loadChilds() {
    this.childService.getAllChildren().subscribe({
      next: data => this.childrens.set(data),
      error: error => this.childrens.set([])
    })
  }

  // Recherche API
  filteredChildren = computed(() => {
    const search = this.searchValue().toLowerCase().trim();
    if (!search) return this.childrens();

    return this.childrens().filter(child =>
      child.firstName.toLowerCase().includes(search) ||
      child.lastName.toLowerCase().includes(search)
    );
  });

}
