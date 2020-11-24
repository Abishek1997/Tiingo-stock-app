function init (input) {

  // split the data set into ohlc and volume
  var data = JSON.parse(input)
  var ohlc = [],
    volume = [],
    dataLength = data.length,
    // set the allowed units for data grouping
    groupingUnits = [[
      'week',             // unit name
      [1]               // allowed multiples
    ], [
      'month',
      [1, 2, 3, 4, 6]
    ]],

    i = 0;

  for (i; i < dataLength; i += 1) {
    ohlc.push([
      data[i]["date"], // the date
      data[i]["open"], // open
      data[i]["high"], // high
      data[i]["low"], // low
      data[i]["close"] // close
    ]);

    volume.push([
      data[i]["date"], // the date
      data[i]["volume"] // the volume
    ]);
  }


  // create the chart
  Highcharts.stockChart('container', {

    rangeSelector: {
      selected: 2
    },
    yAxis: [{
      startOnTick: false,
      endOnTick: false,
      labels: {
        align: 'right',
        x: -3
      },
      title: {
        text: 'OHLC'
      },
      height: '60%',
      lineWidth: 2,
      resize: {
        enabled: true
      }
    }, {
      labels: {
        align: 'right',
        x: -3
      },
      title: {
        text: 'Volume'
      },
      top: '65%',
      height: '35%',
      offset: 0,
      lineWidth: 2
    }],

    tooltip: {
      split: true
    },

    plotOptions: {
      series: {
        dataGrouping: {
          units: groupingUnits
        }
      }
    },

    series: [{
      type: 'candlestick',
      name: 'AAPL',
      id: 'aapl',
      zIndex: 2,
      data: ohlc
    }, {
      type: 'column',
      name: 'Volume',
      id: 'volume',
      data: volume,
      yAxis: 1
    }, {
      type: 'vbp',
      linkedTo: 'aapl',
      params: {
        volumeSeriesID: 'volume'
      },
      dataLabels: {
        enabled: false
      },
      zoneLines: {
        enabled: false
      }
    }, {
      type: 'sma',
      linkedTo: 'aapl',
      zIndex: 1,
      marker: {
        enabled: false
      }
    }]
  });
};