import { Component } from '@angular/core';

@Component({
  selector: 'app-admin-dashboard',
  standalone: false,
  template: `
    <div class="dashboard-content">
      <h2>Admin Dashboard</h2>
      <p>Welcome to the admin dashboard. Use the navigation above to manage users, loans, and view audit trails.</p>
      
      <div class="stats-grid">
        <div class="stat-card">
          <h3>Quick Stats</h3>
          <p>System overview and statistics will appear here.</p>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .dashboard-content {
      padding: 20px;
      text-align: center;
    }
    
    .stats-grid {
      margin-top: 40px;
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
      gap: 20px;
    }
    
    .stat-card {
      background: white;
      padding: 20px;
      border-radius: 8px;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }
  `]
})
export class AdminDashboardComponent { }
