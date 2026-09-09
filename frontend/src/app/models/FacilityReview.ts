export class FacilityReview {
  id!: number;
  facilityId!: number;
  facilityName?: string;
  athleteId!: number;
  athleteName?: string;
  liked!: boolean;
  comment!: string;
  reviewDate!: string | Date;
}