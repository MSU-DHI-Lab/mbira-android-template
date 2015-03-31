<?php
define('HOST',"rush.matrix.msu.edu");
define('USERNAME',"brandon");
define('PASSWORD',"brandonisst3ll4r");
define('DB',"brandon_dev");

$conn = new PDO('mysql:host='.HOST.';'.'dbname='.DB,USERNAME,PASSWORD);

if(isset($_POST['query_type'])){
    $query_type = $_POST['query_type'];
}
//$query_type = "areas";

if ($query_type == "project") {
    if(isset($_POST['projectID'])) {
        $projectID = $_POST['projectID'];
    }
    //$projectID = 31;

    $sql = 'SELECT * FROM mbira_projects where id = ?';

    $stmt = $conn->prepare($sql);
    $stmt->execute(array($projectID));

    class project {
        public $id;
        public $pid;
        public $name;
        public $description;
        public $image_path;
    }

    $data = new project();
    foreach($stmt as $row) {

        $data->id = $row[0];
        $data->pid = $row[1];
        $data->name = $row[2];
        $data->description = $row[3];
        $data->image_path = $row[4];

    }
    echo (json_encode($data));

}

if ($query_type == "locations") {
    if(isset($_POST['projectID'])) {
        $projectID = $_POST['projectID'];
    }
    //$projectID = 31;

    $sql = 'SELECT * FROM mbira_locations where project_id = ?';

    $stmt = $conn->prepare($sql);
    $stmt->execute(array($projectID));

    class location {
        public $id;
        public $project_id;
        public $exhibit_id;
        public $pid;
        public $sid;
        public $name;
        public $description;
        public $dig_deeper;
        public $latitude;
        public $longitude;
        public $thumb_path;
        public $toggle_dig_deeper;
        public $toggle_media;
        public $toggle_comments;
    }

    $location_array = [];
    foreach($stmt as $row) {
        $data = new location;
        $data->id = $row[0];
        $data->project_id = $row[1];
        $data->exhibit_id = $row[2];
        $data->pid = $row[3];
        $data->sid = $row[4];
        $data->name = $row[5];
        $data->description = $row[6];
        $data->dig_deeper = $row[7];
        $data->latitude = $row[8];
        $data->longitude = $row[9];
        $data->thumb_path = $row[10];
        $data->toggle_dig_deeper = $row[11];
        $data->toggle_media = $row[12];
        $data->toggle_comments = $row[13];
        array_push($location_array, $data);
    }
    echo (json_encode(array("item" => $location_array)));

}

if ($query_type == "areas") {
    if(isset($_POST['projectID'])) {
        $projectID = $_POST['projectID'];
    }
    //$projectID = 31;

    $sql = 'SELECT * FROM mbira_areas where project_id = ?';

    $stmt = $conn->prepare($sql);
    $stmt->execute(array($projectID));

    class area {
        public $id;
        public $project_id;
        public $exhibit_id;
        public $name;
        public $description;
        public $dig_deeper;
        public $coordinates;
        public $radius;
        public $shape;
        public $thumb_path;
        public $toggle_dig_deeper;
        public $toggle_media;
        public $toggle_comments;
    }
    $coordinate_array = [];
    class coordinate {
        public $x;
        public $y;
    }

    $area_array = [];
    foreach($stmt as $row) {
        $data = new area;
        $data->id = $row[0];
        $data->project_id = $row[1];
        $data->exhibit_id = $row[2];
        $data->name = $row[3];
        $data->description = $row[4];
        $data->dig_deeper = $row[5];
        foreach(json_decode($row[6]) as $coord) {
            $coordinate = new coordinate;
            $coordinate->x = $coord[0];
            $coordinate->y = $coord[1];
            array_push($coordinate_array, $coordinate);
        }
        $data->coordinates = $coordinate_array;
        $data->radius = $row[7];
        $data->shape = $row[8];
        $data->thumb_path = $row[9];
        $data->toggle_dig_deeper = $row[10];
        $data->toggle_media = $row[11];
        $data->toggle_comments = $row[12];
        array_push($area_array, $data);
    }
    echo (json_encode(array("item" => $area_array)));

}

if ($query_type == "exhibits") {
    if(isset($_POST['projectID'])) {
        $projectID = $_POST['projectID'];
    }
    //$projectID = 31;

    $sql = 'SELECT * FROM mbira_exhibits where project_id = ?';

    $stmt = $conn->prepare($sql);
    $stmt->execute(array($projectID));

    class exhibit {
        public $id;
        public $project_id;
        public $pid;
        public $name;
        public $description;
        public $thumb_path;
    }

    $exhibit_array = [];
    foreach($stmt as $row) {
        $data = new exhibit;

        $data->id = $row[0];
        $data->project_id = $row[1];
        $data->pid = $row[2];
        $data->name = $row[3];
        $data->description = $row[4];
        $data->thumb_path = $row[5];
        array_push($exhibit_array, $data);
    }
    echo (json_encode(array("item" => $exhibit_array)));

}

if ($query_type == "explorations") {
    if(isset($_POST['projectID'])) {
        $projectID = $_POST['projectID'];
    }
    //$projectID = 31;

    $sql = 'SELECT * FROM mbira_explorations where project_id = ?';

    $stmt = $conn->prepare($sql);
    $stmt->execute(array($projectID));

    class exploration {
        public $id;
        public $project_id;
        public $pid;
        public $name;
        public $description;
        public $direction;
        public $thumb_path;
        public $toggle_comments;
        public $toggle_media;
    }

    $exploration_array = [];
    foreach($stmt as $row) {
        $data = new exploration;

        $data->id = $row[0];
        $data->project_id = $row[1];
        $data->pid = $row[2];
        $data->name = $row[3];
        $data->description = $row[4];

        $data->direction = $row[5];

        $data->thumb_path = $row[6];
        $data->toggle_comments = $row[7];
        $data->toggle_media = $row[8];

        array_push($exploration_array, $data);
    }
    echo (json_encode(array("item" => $exploration_array)));

}



?>