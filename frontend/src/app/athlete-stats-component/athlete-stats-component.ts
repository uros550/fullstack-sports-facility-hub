import { Component, ElementRef, inject, OnInit, ViewChild } from '@angular/core';
import { Chart, registerables } from 'chart.js';
import { StatsService } from '../services/stats-service';
import { SportReservedStats } from '../models/SportReservationStats';
import { MonthlyActivity } from '../models/MonthlyActivity';
import { EquipmentSpending } from '../models/EquipmentSpending';

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
      this.loadMonthlyActivity();
    } else if (tab === 'spending') {
      this.loadEquipmentSpending();
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

  loadMonthlyActivity() {
    this.statsService.getMonthlyActivity().subscribe((data) => {
      setTimeout(() => {
        if (this.playedReservedChartCanvas) {
          this.renderLineChart(data);
        }
      }, 0);
    });
  }

  loadEquipmentSpending() {
    this.statsService.getEquipmentSpending().subscribe((data) => {
      setTimeout(() => {
        if (this.playedReservedChartCanvas) {
          this.renderDoughnutChart(data);
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
            backgroundColor: 'rgba(15, 23, 42, 0.9)',
            borderColor: 'rgba(15, 23, 42, 1)',
            borderWidth: 1,
          },
          {
            label: 'Played',
            data: playedData,
            backgroundColor: 'rgba(16, 185, 129, 0.9)',
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
  
  renderLineChart(monthlyData: MonthlyActivity[]) {
    const labels = monthlyData.map((item) => item.month);
    const activityData = monthlyData.map((item) => item.activityCount);

    this.chart = new Chart(this.playedReservedChartCanvas.nativeElement, {
      type: 'line',
      data: {
        labels: labels,
        datasets: [
          {
            label: 'Monthly Activities',
            data: activityData,
            fill: true,
            borderColor: 'rgba(16, 185, 129, 1)',
            backgroundColor: 'rgba(16, 185, 129, 0.15)',
            borderWidth: 2,
            tension: 0.35,
            pointBackgroundColor: 'rgba(15, 23, 42, 1)',
            pointBorderColor: 'rgb(209, 190, 190)',
            pointRadius: 5,
            pointHoverRadius: 7,
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

  renderDoughnutChart(spendingData: EquipmentSpending[]) {
    const labels = spendingData.map((item) => item.month);
    const data = spendingData.map((item) => item.totalSpent);

    //get total spent sum
    const totalSpent = spendingData.reduce((sum, item) => sum + item.totalSpent, 0);

    //center text inside the doughnut
    const centerTextPlugin = {
      id: 'centerText',
      beforeDraw(chart: any) {
        const { ctx, chartArea } = chart;
        if (!chartArea) return;

        const centerX = (chartArea.left + chartArea.right) / 2;
        const centerY = (chartArea.top + chartArea.bottom) / 2;

        ctx.save();

        ctx.font = '600 14px sans-serif';
        ctx.fillStyle = 'rgb(100, 116, 139)';
        ctx.textAlign = 'center';
        ctx.textBaseline = 'middle';
        ctx.fillText('Total', centerX, centerY - 12);

        ctx.font = 'bold 20px sans-serif';
        ctx.fillStyle = 'rgb(15, 23, 42)';
        ctx.fillText(`${totalSpent.toFixed(2)} RSD`, centerX, centerY + 12);

        ctx.restore();
      }
    };

    this.chart = new Chart(this.playedReservedChartCanvas.nativeElement, {
      type: 'doughnut',
      data: {
        labels: labels,
        datasets: [
          {
            label: 'Spending (RSD)',
            data: data,
            backgroundColor: [
              'rgb(15, 23, 42)',
              'rgb(16, 185, 129)',
              'rgb(59, 130, 246)',
              'rgb(245, 158, 11)',
              'rgb(239, 68, 68)',
              'rgb(168, 85, 247)'
            ],
            borderWidth: 2,
            borderColor: 'rgb(255, 255, 255)',
          },
        ],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            position: 'bottom',
          },
        },
      },
      plugins: [centerTextPlugin]
    });
  }
}
