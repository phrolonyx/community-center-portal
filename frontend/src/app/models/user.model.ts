// Correspond exactement au UserResponseDto du backend
export interface User {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  roleName: string; // 'PARENT' | 'ADMIN' | 'ANIMATEUR'
  enabled: boolean;
  pending: boolean;
  createdAt: string;
  lastLogin: string | null;
}
