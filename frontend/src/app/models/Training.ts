export class Training {
  id!: number;
  trainerId!: number;
  trainerName!: string;
  athleteId!: number;
  athleteUsername!: string;
  facilityId!: number;
  facilityName!: string;
  courtId!: number;
  courtName!: string;
  startTime!: string;
  endTime!: string;
  price!: number;
  status!: 'PENDING' | 'CONFIRMED' | 'CANCELLED' | 'DIDNT_SHOW' | 'COMPLETED';
}