export class Court {
  id!: number;
  facilityId!: number;
  sportId!: number;
  name!: string;
  type!: 'OPEN' | 'CLOSED';
  capacity!: number;
  equipmentDescription!: string;
  pricePerHour!: number;
  sportName!: string;
}