/** @Author: Murphy Studebaker
  * Adapted from https://www.simplifiedcoding.net/android-mysql-tutorial-to-perform-basic-crud-operation/
  * This class receives API calls, calls the apporpriate query method, and returns a JSON array 
*/

<?php
 require_once '../includes/DbOperation.php';

 //function validating all the paramters are available
 //we will pass the required parameters to this function
 function isTheseParametersAvailable($params){
 //assuming all parameters are available
   $available = true;
   $missingparams = "";

 foreach($params as $param){
   if(!isset($_POST[$param]) || strlen($_POST[$param])<=0){
     $available = false;
     $missingparams = $missingparams . ", " . $param;
   }
 }

 //if parameters are missing
 if(!$available){
   $response = array();
   $response['error'] = true;
   $response['message'] = 'Parameters ' . substr($missingparams, 1, strlen($missingparams)) . ' missing';

   //displaying error
   echo json_encode($response);

   //stopping further execution
   die();
   }
 }

 //an array to display response
 $response = array();

 //if it is an api call
 //that means a get parameter named api call is set in the URL
 //and with this parameter we are concluding that it is an api call
 if(isset($_GET['apicall'])){
   switch($_GET['apicall']){
     case 'getcoffeeshops':
       $db = new DbOperation();
       $response['error'] = false;
       $response['message'] = 'Request successfully completed';
       $response['coffeeshops'] = $db->getCoffeeShops();
       break;
     //case "moreOptionsBasedOnCalls":
        //carry out different queries here
    }
  } else {
    //if it is not api call
    $response['error'] = true;
    $response['message'] = 'Invalid API Call';
  }

  //displaying the response in json structure
  echo json_encode($response);
