import {ChangeDetectorRef, Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {StatisticsService} from "../../../services/statistics.service";

@Component({
  selector: 'app-statistics',
  templateUrl: './statistics.component.html',
  styleUrls: ['./statistics.component.css']
})
export class StatisticsComponent implements OnInit {
  @ViewChild("myRatio") elementView: ElementRef;
  public doughnutChartWinsLabels: string[] = [];
  public doughnutChartBidsLabels: string[] = [];
  public showBidsChart: boolean = true;
  public showBidsStatistics: boolean = true;
  public showWinsChart: boolean = true;
  public showRatioChart: boolean = true;
  public doughnutChartWinsData: number[] = [];
  public doughnutChartBidsData: number[] = [];
  public doughnutChartRatioData: number[] = [];
  public doughnutChartRatioLabels: string[] = [];
  public doughnutChartType = 'doughnut';
  public myRatio: string = "";

  public barChartOptions = {
    scaleShowVerticalLines: false,
    responsive: true,
    scales: {
      yAxes: [{
        ticks: {
          beginAtZero: true
        }
      }]
    },
    legend: false
  };

  public barChartType = 'bar';
  public barChartLegend: boolean;
  //bar chart popularity
  public barChartPopularityLabels: string[] = [];
  public barChartPopularityData: number[] = [];
  public showPopularity: boolean = true;
  //bar chart success
  public barChartSuccessLabels: string[] = [];
  public barChartSuccessData: number[] = [];
  public showSuccess: boolean = true;
  public showAuctionStatistics: boolean = true;
  public chartColorsTwo: any[] = [
    {
      backgroundColor: ["#6FC8CE", "#ffffff"]
    }];
  public chartColors: any[] = [
    {
      backgroundColor: ["#be5bac", "#7e46c4", "#4b5db7",
        "#3da2bf", "#47b787", "#519240",
        "#fffc51", "#fa7d00", "#ff0000", "#f8006c"]
    }];
  public options: any = {
    legend: {position: 'right',
      labels: {boxWidth:10, fontSize:10}},
    responsive: true,
  }

  constructor(private statisticsService: StatisticsService, private cdRef: ChangeDetectorRef) {
  }

  ngOnInit(): void {
    // get Bids statistics
    //  get Bids per category
    this.statisticsService.getBidStatistics().subscribe(x => {
      this.doughnutChartBidsLabels = Object.keys(x)
      this.doughnutChartBidsData = Object.values(x)
      if (this.doughnutChartBidsLabels.length != 0) {
        this.showBidsChart = false;
      } else {
        this.showBidsChart = true;

      }

    }, error => {
      this.showBidsChart = true;
    })
    //  get Wins per category
    this.statisticsService.getWinStatistics().subscribe(x => {

      this.doughnutChartWinsLabels = Object.keys(x)
      this.doughnutChartWinsData = Object.values(x)
      if (this.doughnutChartWinsLabels.length != 0) {
        this.showWinsChart = false;
      } else {
        this.showWinsChart = true;
      }

    }, error => {
      this.showWinsChart = true;
    })
    //  get Wins-Bids ratio
    this.statisticsService.getBidWinRatio().subscribe(x => {
      this.doughnutChartRatioData = Object.values(x)
      this.doughnutChartRatioLabels = Object.keys(x)
      this.myRatio = this.doughnutChartRatioData[0].toFixed(2) + '%';
      if (this.doughnutChartRatioData.length != 0) {
        this.showRatioChart = false;
      } else {
        this.showRatioChart = true;

      }
    }, error => {
      this.showRatioChart = true;
    })
    this.showBidsStatistics = this.showRatioChart && this.showWinsChart && this.showBidsChart;
    //Get Auction Statistics
    //  get Auction popularity per category
    this.statisticsService.getAuctionPopularity().subscribe(x => {

      this.barChartPopularityLabels = Object.keys(x)
      this.barChartPopularityData = Object.values(x)
      if (this.barChartPopularityLabels.length != 0) {
        this.showPopularity = false;
      } else {
        this.showPopularity = true;

      }

    }, error => {
      this.showSuccess = true;
    })
    //  get Auction success per category
    this.statisticsService.getSuccessOfAuctions().subscribe(x => {

      this.barChartSuccessLabels = Object.keys(x)
      this.barChartSuccessData = Object.values(x)
      if (this.barChartSuccessData.length != 0) {
        this.showSuccess = false;
      } else {
        this.showSuccess = true;
      }

    }, error => {
      this.showSuccess = true;

    })
    this.cdRef.detectChanges();
  }

}
