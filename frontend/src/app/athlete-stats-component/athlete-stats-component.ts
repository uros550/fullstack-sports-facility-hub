import { Component, ElementRef, inject, OnInit, ViewChild } from '@angular/core';
import { Chart, registerables } from 'chart.js';
import { StatsService } from '../services/stats-service';
import { SportReservedStats } from '../models/SportReservationStats';

Chart.register(...registerables);

@Component({
  selector: 'app-athlete-stats-component',
  imports: [],
  templateUrl: './athlete-stats-component.html',
  styleUrl: './athlete-stats-component.css',
})
export class AthleteStatsComponent implements OnInit {

  @ViewChild('playedReservedChart') playedReservedChartCanvas!: ElementRef<HTMLCanvasElement>;

  activeTab: 'reservations' | 'monthly' | 'spending' = 'reservations';
  chart: Chart | null = null;

  private statsService = inject(StatsService);

  ngOnInit(): void {
    this.switchTab('reservations');
  }

  switchTab(tab: 'reservations' | 'monthly' | 'spending') {
    this.activeTab = tab;

    //destroy existing before creating new
    if (this.chart) {
      this.chart.destroy();
      this.chart = null;
    }

    if (tab === 'reservations') {
      this.loadPlayedReservedStats();
    } else if (tab === 'monthly') {
      //later monthly trend
    } else if (tab === 'spending') {
      //later spending ring
    }
  }

  loadPlayedReservedStats() {
    this.statsService.getPlayedReservedPerSport().subscribe((data) => {
      setTimeout(() => {
        if (this.playedReservedChartCanvas) {
          this.renderBarChart(data);
        }
      }, 0);
    });
  }

  renderBarChart(statsData: SportReservedStats[]) {
    const labels = statsData.map((item) => item.sportName);
    const reservedData = statsData.map((item) => item.reservedCount);
    const playedData = statsData.map((item) => item.playedCount);

    this.chart = new Chart(this.playedReservedChartCanvas.nativeElement, {
      type: 'bar',
      data: {
        labels: labels,
        datasets: [
          {
            label: 'Reserved',
            data: reservedData,
            backgroundColor: 'rgba(15, 23, 42, 0.6)',
            borderColor: 'rgba(15, 23, 42, 1)',
            borderWidth: 1,
          },
          {
            label: 'Played',
            data: playedData,
            backgroundColor: 'rgba(16, 185, 129, 0.6)',
            borderColor: 'rgba(16, 185, 129, 1)',
            borderWidth: 1,
          },
        ],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
          y: {
            beginAtZero: true,
            ticks: {
              stepSize: 1,
            },
          },
        },
      },
    });
}
  

}
