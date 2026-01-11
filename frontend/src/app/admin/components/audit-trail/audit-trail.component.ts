import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { AdminService } from '../../services/admin.service';
import { AuditTrail } from '../../models/user.model';

@Component({
  selector: 'app-audit-trail',
  standalone: false,
  template: `
    <div class="audit-trail">
      <h2>Audit Trail</h2>
      
      <!-- Filter Controls -->
      <div class="filter-section">
        <div class="form-group">
          <label for="loanFilter">Filter by Loan ID:</label>
          <input id="loanFilter" type="text" [(ngModel)]="loanFilter" 
                 (ngModelChange)="onFilterChange()" 
                 placeholder="Enter loan ID..." 
                 class="form-control">
        </div>
        <button (click)="clearFilter()" class="btn btn-secondary">Clear Filter</button>
      </div>
      
      <!-- Audit Trail List -->
      <div *ngIf="isLoading" class="loading">Loading audit trail...</div>
      <div *ngIf="!isLoading && auditLogs.length === 0" class="no-data">No audit logs found</div>
      
      <div *ngIf="auditLogs.length > 0" class="audit-list">
        <div *ngFor="let log of auditLogs" class="audit-item">
          <div class="audit-header">
            <span class="action-badge" [class]="getActionClass(log.action)">
              {{ log.action }}
            </span>
            <span class="timestamp">{{ formatDate(log.timestamp) }}</span>
          </div>
          
          <div class="audit-details">
            <div class="detail-row">
              <strong>User:</strong> {{ log.userEmail }}
            </div>
            <div *ngIf="log.loanId" class="detail-row">
              <strong>Loan ID:</strong> {{ log.loanId }}
            </div>
            <div *ngIf="log.comments" class="detail-row">
              <strong>Comments:</strong> {{ log.comments }}
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .audit-trail {
      padding: 20px;
      max-width: 1200px;
      margin: 0 auto;
    }
    
    .filter-section {
      background: #f8f9fa;
      padding: 20px;
      border-radius: 8px;
      margin-bottom: 30px;
      display: flex;
      gap: 15px;
      align-items: end;
    }
    
    .form-group {
      flex: 1;
      margin-bottom: 0;
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
    
    .btn {
      padding: 8px 16px;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      font-size: 14px;
    }
    
    .btn-secondary {
      background: #6c757d;
      color: white;
    }
    
    .audit-list {
      display: flex;
      flex-direction: column;
      gap: 15px;
    }
    
    .audit-item {
      background: white;
      border: 1px solid #e9ecef;
      border-radius: 8px;
      padding: 20px;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }
    
    .audit-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 15px;
    }
    
    .action-badge {
      padding: 6px 12px;
      border-radius: 20px;
      font-size: 12px;
      font-weight: 600;
      text-transform: uppercase;
    }
    
    .action-badge.CREATED {
      background: #28a745;
      color: white;
    }
    
    .action-badge.UPDATED {
      background: #007bff;
      color: white;
    }
    
    .action-badge.SUBMITTED {
      background: #ffc107;
      color: #212529;
    }
    
    .action-badge.APPROVED {
      background: #28a745;
      color: white;
    }
    
    .action-badge.REJECTED {
      background: #dc3545;
      color: white;
    }
    
    .action-badge.DELETED {
      background: #6c757d;
      color: white;
    }
    
    .action-badge.PRICED,
    .action-badge.UNDER_REVIEW {
      background: #17a2b8;
      color: white;
    }
    
    .timestamp {
      color: #6c757d;
      font-size: 14px;
    }
    
    .audit-details {
      display: flex;
      flex-direction: column;
      gap: 8px;
    }
    
    .detail-row {
      font-size: 14px;
    }
    
    .detail-row strong {
      color: #495057;
      min-width: 80px;
      display: inline-block;
    }
    
    .loading, .no-data {
      text-align: center;
      padding: 40px;
      color: #6c757d;
    }
  `]
})
export class AuditTrailComponent implements OnInit {
  auditLogs: AuditTrail[] = [];
  isLoading = false;
  loanFilter = '';

  constructor(
    private adminService: AdminService,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.loadAuditTrail();
  }

  loadAuditTrail(): void {
    this.isLoading = true;
    
    if (this.loanFilter.trim()) {
      this.adminService.getAuditTrailForLoan(this.loanFilter.trim()).subscribe({
        next: (logs) => {
          this.auditLogs = logs;
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Error loading audit trail for loan:', error);
          this.isLoading = false;
        }
      });
    } else {
      this.adminService.getAuditTrail().subscribe({
        next: (logs) => {
          this.auditLogs = logs;
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Error loading audit trail:', error);
          this.isLoading = false;
        }
      });
    }
  }

  onFilterChange(): void {
    this.loadAuditTrail();
  }

  clearFilter(): void {
    this.loanFilter = '';
    this.loadAuditTrail();
  }

  getActionClass(action: string): string {
    return action.replace('_', '').toLowerCase();
  }

  formatDate(dateString: string): string {
    const date = new Date(dateString);
    return date.toLocaleString();
  }
}
