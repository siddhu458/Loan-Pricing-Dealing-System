export interface User {
  id: string;
  email: string;
  role: 'USER' | 'ADMIN';
  active: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface UserCreate {
  email: string;
  password: string;
  role: 'USER' | 'ADMIN';
}

export interface AuditTrail {
  id: string;
  loanId: string;
  userEmail: string;
  action: string;
  comments: string;
  timestamp: string;
}
