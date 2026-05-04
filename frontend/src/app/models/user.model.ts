// Correspond exactement à UserResponseDto.java
export interface User {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  roleName: string; // 'PARENT' | 'ADMIN' | 'ANIMATEUR'
  enabled: boolean | null;
  pending: boolean | null;
  createdAt: string;
  lastLogin: string | null;
}
