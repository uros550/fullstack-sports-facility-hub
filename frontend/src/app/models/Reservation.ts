export class Reservation {
  id!: number;
  facilityId!: number;
  facilityName!: string;
  city!: string;
  courtId!: number;
  courtName!: string;
  athleteId!: number;
  sportId!: number;
  sportName!: string;
  startTime!: string;
  endTime!: string;
  status!: string;
  missingPlayers!: number;
}