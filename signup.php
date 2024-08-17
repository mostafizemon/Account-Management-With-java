<?php
if (!empty($_POST['fullname']) && !empty($_POST['email']) && !empty($_POST['password']) && !empty($_POST['address'])){
    $con=mysqli_connect('localhost','root','','profile_management');
    $fullname=$_POST['fullname'];
    $email=$_POST['email'];
    $password=password_hash($_POST['password'],PASSWORD_DEFAULT);
    $address=$_POST['address'];
    $decrypted_email=decryptDta($email);

    if ($con){
        // Check if email or phone already exists
        $checkQuery = "SELECT * FROM signup WHERE email = '$decrypted_email'";
        $result = mysqli_query($con, $checkQuery);
        $row = mysqli_fetch_array($result, MYSQLI_ASSOC);

        if(mysqli_num_rows($result) > 0) {
            if ($row['email'] == $decrypted_email) {
                echo "Email already registered";
            }
        } else {
            // Insert new record
            $sql="INSERT INTO signup(fullname, email, address, password) VALUES ('$fullname', '$decrypted_email','$address', '$password')";
            if (mysqli_query($con,$sql)){
                echo "success";
            } else {
                echo "failed";
            }
        }
    } else {
        echo "Database Connection Failed";
    }
} else {
    echo "All Fields Required";
}

function  decryptDta($text){
    $decode=base64_decode($text);
    $decrypted=openssl_decrypt($decode,'AES-128-ECB','qNt)9##Fw#u6&)qa',OPENSSL_RAW_DATA);
    return $decrypted;

}


?>
