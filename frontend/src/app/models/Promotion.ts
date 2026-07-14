export class Promotion {
  id!: number;
  name!: string;
  facilityId!: number;
  facilityName!: string;
  sportId!: number;
  discountType!: 'PERCENTAGE' | 'FIXED';
  discountValue!: number;
  startDate!: string;
  endDate!: string;
}