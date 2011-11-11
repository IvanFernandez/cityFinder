<%@ page contentType="text/html;charset=ISO-8859-1" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
<meta name="layout" content="main"/>
<title>Insert title here</title>
<link href="../../css/jquery.zweatherfeed.css" rel="stylesheet" type="text/css" />
<link href="../../css/jquery.zgooglemap.css" rel="stylesheet" type="text/css" />
<link href="../../css/jquery.zflickrfeed.css" rel="stylesheet" type="text/css" />

<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js" type="text/javascript"></script>
<script src="../../js/jquery.zweatherfeed.min.js" type="text/javascript"></script>
<script src="http://maps.google.com/maps/api/js?sensor=false&region=ES" type="text/javascript"></script>
<script src="../../js/jquery.zgooglemap.min.js" type="text/javascript"></script>
<script src="../../js/jquery.zflickrfeed.min.js" type="text/javascript"></script>
<script type="text/javascript">

var aLocations = new Array();
var aTitles = new Array();
var aDetails = new Array();

aLocations = [
'${cityInstance.name},${cityInstance.name},${cityInstance.country}'
];
aTitles = [
'${cityInstance.name}'
];
aSummary = [
'<h3>${cityInstance.name}</h3><p>${cityInstance.name} is a city of ${cityInstance.country}...</p>'
]; 

$(document).ready(function () {
  $('#test').weatherfeed(['${flash.code}']);
  $('#gmap').GoogleMap(aLocations, aTitles, aSummary, {
	  type: 3,
	  zoom: 9
	});
  $('#flicker').flickrfeed('','${cityInstance.name}', {
	    limit: 5
	  });
});



</script>
</head>
<body>
  <div class="body">
  <div id="test"></div>
  <div id="gmap"></div>
  <div id="flicker"></div>

  
  Information about ${cityInstance.name} with code ${flash.code}
  </div>
</body>
</html>