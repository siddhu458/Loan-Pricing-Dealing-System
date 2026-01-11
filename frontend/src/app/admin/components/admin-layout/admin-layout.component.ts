import { Component } from '@angular/core';

@Component({
  selector: 'app-admin-layout',
  standalone: false,
  template: `
    <div class="admin-layout">
      <div class="admin-header">
        <h1>Admin Dashboard</h1>
        <p>Manage users, loans, and view audit trails</p>
      </div>
      
      <div class="admin-nav">
        <a routerLink="/admin" class="nav-card" routerLinkActive="active">
          <div class="nav-icon">📊</div>
          <h3>Dashboard</h3>
          <p>Admin overview</p>
        </a>
        
        <a routerLink="/admin/users" class="nav-card" routerLinkActive="active">
          <div class="nav-icon">👥</div>
          <h3>User Management</h3>
          <p>Create and manage user accounts</p>
        </a>
        
        <a routerLink="/admin/loans" class="nav-card" routerLinkActive="active">
          <div class="nav-icon">💼</div>
          <h3>Loan Management</h3>
          <p>Review and approve submitted loans</p>
        </a>
        
        <a routerLink="/admin/audit" class="nav-card" routerLinkActive="active">
          <div class="nav-icon">📋</div>
          <h3>Audit Trail</h3>
          <p>View complete system audit logs</p>
        </a>
      </div>
      
      <router-outlet></router-outlet>
    </div>
  `,
  styles: [`
    .admin-layout {
      min-height: 100vh;
      background: #f8f9fa;
      padding: 20px;
    }
    
    .admin-header {
      text-align: center;
      margin-bottom: 40px;
    }
    
    .admin-header h1 {
      color: #2c3e50;
      margin-bottom: 10px;
    }
    
    .admin-header p {
      color: #7f8c8d;
      font-size: 16px;
    }
    
    .admin-nav {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
      gap: 20px;
      margin-bottom: 40px;
    }
    
    .nav-card {
      background: white;
      border: 1px solid #e9ecef;
      border-radius: 12px;
      padding: 20px;
      text-decoration: none;
      color: inherit;
      transition: all 0.3s ease;
      box-shadow: 0 2px 8px rgba(0,0,0,0.1);
    }
    
    .nav-card:hover {
      transform: translateY(-5px);
      box-shadow: 0 8px 25px rgba(0,0,0,0.15);
      border-color: #007bff;
    }
    
    .nav-card.active {
      border-color: #007bff;
      background: #f8f9ff;
    }
    
    .nav-icon {
      font-size: 36px;
      margin-bottom: 10px;
    }
    
    .nav-card h3 {
      color: #2c3e50;
      margin-bottom: 8px;
      font-size: 18px;
    }
    
    .nav-card p {
      color: #7f8c8d;
      font-size: 13px;
      line-height: 1.4;
    }
  `]
})
export class AdminLayoutComponent { }
