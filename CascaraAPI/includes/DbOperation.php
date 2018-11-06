/** @Author: Murphy Studebaker
  * Adapted from https://www.simplifiedcoding.net/android-mysql-tutorial-to-perform-basic-crud-operation/
  * This class defines the interworkings of every DB query operation
*/
<?php

class DbOperation
{
    //Database connection link
    private $con;

    //Class constructor
    function __construct()
    {
        //Getting the DbConnect.php file
        require_once dirname(__FILE__) . '/DbConnect.php';
        //Creating a DbConnect object to connect to the database
        $db = new DbConnect();
        //Initializing our connection link of this class
        //by calling the method connect of DbConnect class
        $this->con = $db->connect();
    }

 /*
 * The create operation
 * When this method is called a new record is created in the database
 function createHero($name, $realname, $rating, $teamaffiliation){
 $stmt = $this->con->prepare("INSERT INTO heroes (name, realname, rating, teamaffiliation) VALUES (?, ?, ?, ?)");
 $stmt->bind_param("ssis", $name, $realname, $rating, $teamaffiliation);
 if($stmt->execute())
 return true;
 return false;
} */

 /*
 * The read operation
 * When this method is called it is returning all the existing record of the database
 */
 function getCoffeeShops(){
   $stmt = $this->con->prepare("SELECT * FROM coffeetemp");
   $stmt->execute();
   $stmt->bind_result($id, $houseName, $coffeeScore, $wifiScore);

   $coffeeShops = array();

   while($stmt->fetch()){
     $shop  = array();
     $shop['id'] = $id;
     $shop['houseName'] = $houseName;
     $shop['coffeeScore'] = $coffeeScore;
     $shop['wifiScore'] = $wifiScore;
     array_push($coffeeShops, $shop);
   }
   return $coffeeShops;
 }

   /*
   * The update operation
   * When this method is called the record with the given id is updated with the new given values
   function updateHero($id, $name, $realname, $rating, $teamaffiliation){
     $stmt = $this->con->prepare("UPDATE heroes SET name = ?, realname = ?, rating = ?, teamaffiliation = ? WHERE id = ?");
     $stmt->bind_param("ssisi", $name, $realname, $rating, $teamaffiliation, $id);
     if($stmt->execute())
     return true;
     return false;
   } */


   /*
   * The delete operation
   * When this method is called record is deleted for the given id
   function deleteHero($id){
     $stmt = $this->con->prepare("DELETE FROM heroes WHERE id = ? ");
     $stmt->bind_param("i", $id);
     if($stmt->execute())
      return true;

    return false;
   }
  }*/
}
