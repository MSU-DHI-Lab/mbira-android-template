<?php
define('HOST',"rush.matrix.msu.edu");
define('USERNAME',"brandon");
define('PASSWORD',"brandonisst3ll4r");
define('DB',"brandon_dev");

if(isset($_POST['query'])){
	$query = $_POST['query'];
}

$conn = mysqli_connect(HOST,USERNAME,PASSWORD,DB);
$sql = 'SELECT image_path FROM mbira_projects where id = ?';

$stmt = $conn->prepare($sql);
$stmt->bind_param('i',$query);
$stmt->execute();

$stmt->bind_result($res);
//$row = mysqli_fetch_array($result);
//$data = $row[0];
if($stmt->fetch()) {
	echo $res;
}
mysqli_close($conn);

?>