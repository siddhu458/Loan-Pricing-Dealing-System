import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AdminService } from '../../services/admin.service';
import { User, UserCreate } from '../../models/user.model';

@Component({
  selector: 'app-user-management',
  standalone: false,
  template: `
    <div class="user-management">
      <h2>User Management</h2>
      
      <!-- Create User Form -->
      <div class="create-user-section">
        <h3>Create New User</h3>
        <form [formGroup]="userForm" (ngSubmit)="createUser()">
          <div class="form-group">
            <label for="email">Email:</label>
            <input id="email" type="email" formControlName="email" class="form-control">
            <div *ngIf="userForm.get('email')?.invalid && userForm.get('email')?.touched" class="error">
              Valid email is required
            </div>
          </div>
          
          <div class="form-group">
            <label for="password">Password:</label>
            <input id="password" type="password" formControlName="password" class="form-control">
            <div *ngIf="userForm.get('password')?.invalid && userForm.get('password')?.touched" class="error">
              Password must be at least 6 characters
            </div>
          </div>
          
          <div class="form-group">
            <label for="role">Role:</label>
            <select id="role" formControlName="role" class="form-control">
              <option value="USER">USER</option>
              <option value="ADMIN">ADMIN</option>
            </select>
          </div>
          
          <button type="submit" [disabled]="userForm.invalid || isCreating" class="btn btn-primary">
            {{ isCreating ? 'Creating...' : 'Create User' }}
          </button>
        </form>
      </div>
      
      <!-- Users List -->
      <div class="users-list-section">
        <h3>Existing Users</h3>
        <div *ngIf="isLoading" class="loading">Loading users...</div>
        <div *ngIf="!isLoading && users.length === 0" class="no-data">No users found</div>
        
        <table *ngIf="users.length > 0" class="users-table">
          <thead>
            <tr>
              <th>Email</th>
              <th>Role</th>
              <th>Status</th>
              <th>Created</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let user of users">
              <td>{{ user.email }}</td>
              <td>
                <span class="role-badge" [class.admin]="user.role === 'ADMIN'">
                  {{ user.role }}
                </span>
              </td>
              <td>
                <span class="status-badge" [class.active]="user.active" [class.inactive]="!user.active">
                  {{ user.active ? 'Active' : 'Inactive' }}
                </span>
              </td>
              <td>{{ formatDate(user.createdAt) }}</td>
              <td>
                <button (click)="toggleUserStatus(user)" 
                        [class]="user.active ? 'btn-warning' : 'btn-success'"
                        class="btn btn-sm">
                  {{ user.active ? 'Deactivate' : 'Activate' }}
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  `,
  styles: [`
    .user-management {
      padding: 20px;
      max-width: 1200px;
      margin: 0 auto;
    }
    
    .create-user-section {
      background: #f8f9fa;
      padding: 20px;
      border-radius: 8px;
      margin-bottom: 30px;
    }
    
    .form-group {
      margin-bottom: 15px;
    }
    
    .form-group label {
      display: block;
      margin-bottom: 5px;
      font-weight: 500;
    }
    
    .form-control {
      width: 100%;
      padding: 8px 12px;
      border: 1px solid #ddd;
      border-radius: 4px;
      font-size: 14px;
    }
    
    .error {
      color: #dc3545;
      font-size: 12px;
      margin-top: 5px;
    }
    
    .btn {
      padding: 8px 16px;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      font-size: 14px;
    }
    
    .btn-primary {
      background: #007bff;
      color: white;
    }
    
    .btn-primary:disabled {
      background: #ccc;
      cursor: not-allowed;
    }
    
    .btn-success {
      background: #28a745;
      color: white;
    }
    
    .btn-warning {
      background: #ffc107;
      color: #212529;
    }
    
    .btn-sm {
      padding: 4px 8px;
      font-size: 12px;
    }
    
    .users-table {
      width: 100%;
      border-collapse: collapse;
      margin-top: 20px;
    }
    
    .users-table th,
    .users-table td {
      padding: 12px;
      text-align: left;
      border-bottom: 1px solid #ddd;
    }
    
    .users-table th {
      background: #f8f9fa;
      font-weight: 600;
    }
    
    .role-badge {
      padding: 4px 8px;
      border-radius: 12px;
      font-size: 12px;
      font-weight: 500;
    }
    
    .role-badge.admin {
      background: #dc3545;
      color: white;
    }
    
    .role-badge:not(.admin) {
      background: #6c757d;
      color: white;
    }
    
    .status-badge {
      padding: 4px 8px;
      border-radius: 12px;
      font-size: 12px;
      font-weight: 500;
    }
    
    .status-badge.active {
      background: #28a745;
      color: white;
    }
    
    .status-badge.inactive {
      background: #6c757d;
      color: white;
    }
    
    .loading, .no-data {
      text-align: center;
      padding: 40px;
      color: #6c757d;
    }
  `]
})
export class UserManagementComponent implements OnInit {
  users: User[] = [];
  userForm: FormGroup;
  isCreating = false;
  isLoading = false;

  constructor(
    private adminService: AdminService,
    private fb: FormBuilder
  ) {
    this.userForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      role: ['USER', Validators.required]
    });
  }

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.isLoading = true;
    this.adminService.getUsers().subscribe({
      next: (users) => {
        this.users = users;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading users:', error);
        this.isLoading = false;
      }
    });
  }

  createUser(): void {
    if (this.userForm.invalid) return;

    this.isCreating = true;
    const userCreate: UserCreate = this.userForm.value;

    this.adminService.createUser(userCreate).subscribe({
      next: (createdUser) => {
        this.users.push(createdUser);
        this.userForm.reset();
        this.isCreating = false;
      },
      error: (error) => {
        console.error('Error creating user:', error);
        this.isCreating = false;
      }
    });
  }

  toggleUserStatus(user: User): void {
    this.adminService.updateUserStatus(user.id, !user.active).subscribe({
      next: (updatedUser) => {
        const index = this.users.findIndex(u => u.id === user.id);
        if (index !== -1) {
          this.users[index] = updatedUser;
        }
      },
      error: (error) => {
        console.error('Error updating user status:', error);
      }
    });
  }

  formatDate(dateString: string): string {
    return new Date(dateString).toLocaleDateString();
  }
}
