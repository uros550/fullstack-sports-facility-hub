export class CalendarEvent {
  id!: number;
  courtId!: number;
  type!: 'RESERVATION' | 'TRAINING';
  startTime!: string;
  endTime!: string;
  date!: string;
}