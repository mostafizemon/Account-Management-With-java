<?php
$image64=$_POST['image'];
$email=$_POST['email'];
$decryptemail=decryptDta($email);
$decodeimage=base64_decode($image64);
$filename=uniqid().'.jpg';
$filepath='image/'.$filename;

if (file_put_contents($filepath,$decodeimage)){
    $con=mysqli_connect('localhost','root','','profile_management');
    if ($con){
        $sql="UPDATE signup SET image='$filepath' WHERE email='$decryptemail'";
        if (mysqli_query($con,$sql)){
            echo "success";
        } else {
            echo "failed";
        }
    }
}
else{

}

function  decryptDta($text){
    $decode=base64_decode($text);
    $decrypted=openssl_decrypt($decode,'AES-128-ECB','qNt)9##Fw#u6&)qa',OPENSSL_RAW_DATA);
    return $decrypted;

}

?>
