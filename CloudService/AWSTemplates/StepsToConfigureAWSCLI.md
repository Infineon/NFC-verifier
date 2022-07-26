# Steps to configure AWS CLI:
The following are the steps to install and configure the prerequisites for using the AWS command line interface (CLI) on your Windows host:

1. Download the [AWS CLI setup](https://awscli.amazonaws.com/AWSCLIV2.msi) file.

2. Open the downloaded AWS CLI setup file and follow the installation screen.
    
    ![AWS-CLI-Configuration-Step-03](../../Documents/Images/AWS-CLI-Configuration-Step-02.png)
    
3. Accept the End-User License Agreement and proceed to install in the default location. 

    ![AWS-CLI-Configuration-Step-03](../../Documents/Images/AWS-CLI-Configuration-Step-03-a.png)

    ![AWS-CLI-Configuration-Step-03](../../Documents/Images/AWS-CLI-Configuration-Step-03-b.png)

4. Complete the installation process and click 'Finish'.

     ![AWS-CLI-Configuration-Step-03](../../Documents/Images/AWS-CLI-Configuration-Step-04-a.png)

     ![AWS-CLI-Configuration-Step-03](../../Documents/Images/AWS-CLI-Configuration-Step-04-b.png)

5. Configure IAM permissions and AWS credentials as follow:

    a. Login to your AWS account and select 'IAM' user service from the service list.
    
    ![AWS-CLI-Configuration-Step-04](../../Documents/Images/AWS-CLI-Configuration-Step-05-a.png)

    b. Select 'User groups' from the 'Access management' and then click the 'Create group' button.

    ![AWS-CLI-Configuration-Step-04](../../Documents/Images/AWS-CLI-Configuration-Step-05-b.png)

    c. Type the name of the group in the text box.
    
    ![AWS-CLI-Configuration-Step-04](../../Documents/Images/AWS-CLI-Configuration-Step-05-c.png)

    d. Select the Permission policy as “AdministratorAccess” using the filter and click the 'Create group' button.
    
    ![AWS-CLI-Configuration-Step-04](../../Documents/Images/AWS-CLI-Configuration-Step-05-d.png)

    e. Select 'Users' from the 'Access management' and click the 'Add users' button..
        
    ![AWS-CLI-Configuration-Step-04](../../Documents/Images/AWS-CLI-Configuration-Step-05-e.png)

    f. Type the user name and select AWS credentials types as 'Access key-programmatic access' and click on 'Next: Permissions' button.

    ![AWS-CLI-Configuration-Step-04](../../Documents/Images/AWS-CLI-Configuration-Step-05-f.png)

    g. To add a user to a group, select the group name you created and then click on 'Next: Tags' button.        
    
    ![AWS-CLI-Configuration-Step-04](../../Documents/Images/AWS-CLI-Configuration-Step-05-g.png)

    h. Adding tags are optional. Click 'Next: Review' button to skip adding tags.  

    i. Review the content and click on 'Create user' button.
        
    ![AWS-CLI-Configuration-Step-04](../../Documents/Images/AWS-CLI-Configuration-Step-05-i.png)

    j. Download the credential CSV file.
    
    ![AWS-CLI-Configuration-Step-04](../../Documents/Images/AWS-CLI-Configuration-Step-05-j.png)

6. Enter the following command to access AWS CLI. The sample values are shown in the example below. Replace 
them with the values from the CSV file.

        $ aws configure
        
        AWS Access Key ID [None]: EXAMPLEEXAMPLEEXAMPLE
        AWS Secret Access Key [None]: EXAMPLEEXAMPLEEXAMPLE
        Default region name [None]: eu-central-1
        Default output format [None]: json
