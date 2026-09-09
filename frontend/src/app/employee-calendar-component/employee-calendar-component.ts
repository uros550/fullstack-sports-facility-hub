import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { forkJoin } from 'rxjs';

import { User } from '../models/User';
import { SportsFacilityService } from '../services/sports-facility-service';
import { SportsFacility } from '../models/SportsFacility';
import { Court } from '../models/Court';
import { UserService } from '../services/user-service';
import { AvailabilitySlot } from '../models/AvailabilitySlot';

@Component({
  selector: 'app-employee-calendar-component',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './employee-calendar-component.html',
  styleUrl: './employee-calendar-component.css',
})
export class EmployeeCalendarComponent implements OnInit {

  currentUser: User | null = null;
  facilities: SportsFacility[] = [];
  courts: Court[] = [];

  selectedFacilityId: number = 0;
  selectedCourtId: number = 0;

  currentWeekStart!: Date;
  weekDays: Date[] = [];

  timeSlots: string[] = [];

  weeklyAvailability: { [dateStr: string]: AvailabilitySlot[] } = {};

  private facilityService = inject(SportsFacilityService);
  private userService = inject(UserService);

  ngOnInit() {
    this.setWeekToToday();

    const userJson = localStorage.getItem('loggedUser');
    if (userJson) {
      try {
        this.currentUser = JSON.parse(userJson);
      } catch (error) {
        console.error('Local storage getItem error:', error);
      }
    }

    if (this.currentUser) {
      this.facilityService.getFacilitiesForEmployee(this.currentUser.id).subscribe(data => {
        this.facilities = data;
        if (this.facilities.length > 0) {
          this.selectedFacilityId = this.facilities[0].id;
          this.onFacilityChange();
        }
      });
    }
  }

  onFacilityChange() {
    if (!this.selectedFacilityId) return;

    this.facilityService.getAllCourtsById(this.selectedFacilityId).subscribe(data => {
      this.courts = data;
      if (this.courts.length > 0) {
        this.selectedCourtId = this.courts[0].id;
        this.onCourtChange();
      } else {
        this.selectedCourtId = 0;
        this.weeklyAvailability = {};
        this.timeSlots = [];
      }
    });
  }

  onCourtChange() {
    if (!this.selectedCourtId) return;
    this.loadWeeklyAvailability();
  }

  loadWeeklyAvailability() {
    if (!this.selectedCourtId || this.weekDays.length === 0) return;

    const requests = this.weekDays.map(day => {
      const dateStr = this.formatDateKey(day);
      return this.userService.getCourtAvailability(this.selectedCourtId, dateStr);
    });

    forkJoin(requests).subscribe({
      next: (results: AvailabilitySlot[][]) => {
        this.weeklyAvailability = {};
        const slotTimesSet = new Set<string>();

        results.forEach((daySlots, index) => {
          const dateStr = this.formatDateKey(this.weekDays[index]);
          this.weeklyAvailability[dateStr] = daySlots;

          daySlots.forEach(slot => slotTimesSet.add(slot.startTime));
        });

        this.timeSlots = Array.from(slotTimesSet).sort();
      },
      error: (err) => console.error('Error:', err)
    });
  }

  getSlot(day: Date, startTime: string): AvailabilitySlot | undefined {
    const dateStr = this.formatDateKey(day);
    const daySlots = this.weeklyAvailability[dateStr];
    return daySlots?.find(s => s.startTime === startTime);
  }

  setWeekToToday() {
    const today = new Date();
    const dayOfWeek = today.getDay(); // 0 = Sun, 1 = Mon...
    const distanceToMonday = dayOfWeek === 0 ? -6 : 1 - dayOfWeek;

    this.currentWeekStart = new Date(today);
    this.currentWeekStart.setDate(today.getDate() + distanceToMonday);
    this.generateWeekDays();
  }

  generateWeekDays() {
    this.weekDays = [];
    for (let i = 0; i < 7; i++) {
      const day = new Date(this.currentWeekStart);
      day.setDate(this.currentWeekStart.getDate() + i);
      this.weekDays.push(day);
    }
  }

  prevWeek() {
    this.currentWeekStart.setDate(this.currentWeekStart.getDate() - 7);
    this.generateWeekDays();
    if (this.selectedCourtId) this.loadWeeklyAvailability();
  }

  nextWeek() {
    this.currentWeekStart.setDate(this.currentWeekStart.getDate() + 7);
    this.generateWeekDays();
    if (this.selectedCourtId) this.loadWeeklyAvailability();
  }

  formatDateKey(date: Date): string {
    const y = date.getFullYear();
    const m = String(date.getMonth() + 1).padStart(2, '0');
    const d = String(date.getDate()).padStart(2, '0');
    return `${y}-${m}-${d}`;
  }

  getDayHeader(day: Date): string {
    const dayNames = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'];
    
    const index = (day.getDay() + 6) % 7;
    const name = dayNames[index];

    const d = String(day.getDate()).padStart(2, '0');
    const m = String(day.getMonth() + 1).padStart(2, '0');

    return `${name} ${d}.${m}.`;
  }

  getWeekRangeLabel(): string {
    if (!this.weekDays || this.weekDays.length === 0) return '';
    const start = this.weekDays[0];
    const end = this.weekDays[6];
    const fmt = (d: Date) => `${String(d.getDate()).padStart(2, '0')}.${String(d.getMonth() + 1).padStart(2, '0')}.`;
    return `${fmt(start)} - ${fmt(end)}`;
  }

}