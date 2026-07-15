export class User {
  id!: number;
  firstName!: string;
  lastName!: string;
  username!: string;
  email!: string;
  password!: string;
  phone!: string;
  profilePicture!: string;
  role!: 'ATHLETE' | 'EMPLOYEE' | 'ADMIN';
  status!: 'APPROVED' | 'PENDING';
}