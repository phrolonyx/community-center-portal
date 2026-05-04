import {ChangeDetectionStrategy, Component, OnInit, signal} from '@angular/core';
import {UserService} from '../../../apiServices/user-service';

@Component({
  selector: 'app-dashboard-admin',
  standalone: false,
  templateUrl: './dashboard-admin.html',
  styleUrl: './dashboard-admin.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class DashboardAdmin implements OnInit {

  totalUsers = signal(0);
  totalParents = signal(0);
  totalAnimateurs = signal(0);
  totalDisabled = signal(0);

  pendingUsers = signal<any[]>([]);

  constructor(private userService: UserService) {}

  ngOnInit() {
    this.loadDashboard();
  }

  loadDashboard() {
    this.userService.getAdminDashboard().subscribe({
      next: data => {
        this.totalUsers.set(data.totalUsers);
        this.totalParents.set(data.totalParents);
        this.totalAnimateurs.set(data.totalAnimateurs);
        this.totalDisabled.set(data.totalDisabled);
        this.pendingUsers.set(data.pendingUsers);
      },
      error: err => console.error("Erreur dashboard admin :", err)
    });
  }

  approveUser(id: number) {
    this.userService.approveUser(id).subscribe({
      next: () => this.loadDashboard()
    });
  }

  rejectUser(id: number) {
    this.userService.rejectUser(id).subscribe({
      next: () => this.loadDashboard()
    });
  }
}
