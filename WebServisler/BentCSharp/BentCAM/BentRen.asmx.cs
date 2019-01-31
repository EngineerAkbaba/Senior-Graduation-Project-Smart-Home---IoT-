using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data.SqlClient;
using System.Linq;
using System.Net;
using System.Net.Mail;
using System.Web;
using System.Web.Services;

namespace BentCAM
{

    /// <summary>
    /// Summary description for BentRen
    /// </summary>
    [WebService(Namespace = "http://demo.android.org/")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [System.ComponentModel.ToolboxItem(false)]
    // To allow this Web Service to be called from script, using ASP.NET AJAX, uncomment the following line. 
    // [System.Web.Script.Services.ScriptService]
    public class BentRen : System.Web.Services.WebService
    {
        public struct devices
        {
            public int DeviceId;
            public string name;
            public string type;
            public string topicname;

        };

        public static SqlConnection con = new SqlConnection(@"Data Source=SERVER;Initial Catalog=PIA2016;persist security info=True; user id=sa;password=qw-1;multipleactiveresultsets=True");
        [WebMethod]
        public  bool CreateNewUser(string email, string password)
        {

            SqlCommand cmdSelect;
            SqlCommand cmd;
            bool newuser = true;
            con.Open();
            cmdSelect = new SqlCommand("SELECT COUNT(*) FROM [dbo].[User] WHERE email='" + email + "'", con);

            Int32 count = (Int32)cmdSelect.ExecuteScalar();
            cmdSelect.ExecuteNonQuery();
            if (count == 0)
            {
                cmd = new SqlCommand("INSERT INTO [dbo].[User](email,password) VALUES (@email,@password)", con);
                cmd.Parameters.Add("@email", email);
                cmd.Parameters.Add("@password", password);
                cmd.ExecuteNonQuery();
                con.Close();
                newuser = true;
            }
            else
            {
                newuser = false;
            }
 
            return newuser;
            con.Close();
            cmd.Connection.Close();
            cmdSelect.Connection.Close();
        }
        [WebMethod]
        public  int Sig_in(string email, string password)
        {
            SqlCommand cmdSign;
            int signnumber = 0;
            con.Open();
            cmdSign = new SqlCommand("SELECT COUNT(*) FROM [User] WHERE email='" + email + "' and password='" + password + "'", con);
            Int32 count = (Int32)cmdSign.ExecuteScalar();
            cmdSign.ExecuteNonQuery();
            con.Close();
            con.Open();
            cmdSign = new SqlCommand("SELECT COUNT(*) FROM [User] WHERE email='" + email + "' and password='" + password + "' and status='Kill'", con);
            Int32 status = (Int32)cmdSign.ExecuteScalar();
            cmdSign.ExecuteNonQuery();
            con.Close();
            if ((count == 1) && (status != 1))
            {
                signnumber = 1; //The operation is successful.
            }
            else if ((count == 0))
            {
                signnumber = 0;//This User is not Exist.
            }
            else if (status == 1)
            {
                signnumber = 2;
            }
            return signnumber;
            cmdSign.Connection.Close();
        }
        [WebMethod]
        public  bool Forgot_password(string email)
        {
            SqlCommand cmdpassword;
            bool msg;
            con.Open();
            cmdpassword = new SqlCommand("SELECT password FROM [User] WHERE email='" + email + "'", con);
            SqlDataReader reader = cmdpassword.ExecuteReader();
            
            string password = "";
            while (reader.Read())
            {
                password = reader.GetString(0);
            }
            con.Close();
            if (password.ToString().Equals(""))
            {
                msg = false;
            }
            else
            {
                SmtpClient client = new SmtpClient("smtp.gmail.com", 587);
                client.EnableSsl = true;
                client.Timeout = 10000;
                client.DeliveryMethod = SmtpDeliveryMethod.Network;
                client.UseDefaultCredentials = false;
                client.Credentials = new NetworkCredential("webservice.proje@gmail.com", "2011510049.");
                MailMessage message = new MailMessage();
                message.To.Add(email);
                message.From = new MailAddress("webservice.proje@gmail.com");
                message.Subject = "Your Password This!";
                message.Body = password;
                client.Send(message);
                msg = true;
            }
            cmdpassword.Connection.Close();
            con.Close();
            return msg;
        }

        [WebMethod]
        public List<devices> DeviceList(string email, string password)
        {
            SqlCommand cmddevicelist;
            List<devices> d_list = new List<devices>();
            devices temp = new devices();
            con.Open();
            cmddevicelist = new SqlCommand("SELECT Device.ItemId,Device.Name,Device.Type,Device.topicname FROM Device INNER JOIN [User] ON Device.userid= [User].UserId WHERE email='" + email + "' and password='" + password + "'", con);
            SqlDataReader reader = cmddevicelist.ExecuteReader();
            while (reader.Read())
            {
                temp.DeviceId = Convert.ToInt32(reader[0]);
                temp.name = reader[1].ToString();
                temp.type = reader[2].ToString();
                temp.topicname = reader[3].ToString();
                d_list.Add(temp);
            }
            con.Close();
            cmddevicelist.Connection.Close();
            return d_list;

        }

        [WebMethod]
        public  bool addDevice(string name, string type, string macAddress, string status1, string status2, string status3, int userid,string topicname, string date)
        {
            bool insert = false;
            SqlCommand cmdInsertDevice;
            con.Open();
            cmdInsertDevice = new SqlCommand("INSERT INTO [Item](Name,Type,Mac,Status1,Status2,Status3,userid,topicname,date) VALUES (@name,@type,@macAddress,@status1,@status2,@status3,@userid,@topicname,@date)", con);
            cmdInsertDevice.Parameters.Add("@name", name);
            cmdInsertDevice.Parameters.Add("@type", type);
            cmdInsertDevice.Parameters.Add("@macAddress", macAddress);
            cmdInsertDevice.Parameters.Add("@status1", status1);
            cmdInsertDevice.Parameters.Add("@status2", status2);
            cmdInsertDevice.Parameters.Add("@status3", status3);
            cmdInsertDevice.Parameters.Add("@userid", userid);
            cmdInsertDevice.Parameters.Add("@topicname", topicname);
            cmdInsertDevice.Parameters.Add("@date", date);
            cmdInsertDevice.ExecuteNonQuery();
            cmdInsertDevice.Connection.Close();
            con.Close();
            insert = true;
            return insert;

        }
        [WebMethod]
        public  bool KillDevice(string MacAddress)
        {
            bool killDev = false;
            con.Open();
            SqlCommand cmdKillDevice;
            cmdKillDevice = new SqlCommand("DELETE FROM [Item] WHERE Mac='" + MacAddress + "' ", con);
            cmdKillDevice.ExecuteNonQuery();
            cmdKillDevice.Connection.Close();
            con.Close();
            killDev = true;
            return killDev;
        }
        [WebMethod]
        public  string first_connection(string email, string password, string MacAddress)
        {
            SqlCommand cmdKillUser;
            SqlCommand cmdFirstConnect;
            string answer = "";
            con.Open();
            cmdFirstConnect = new SqlCommand("SELECT COUNT(*) FROM [User]  WHERE email='" + email + "'and password='" + password + "'and status='Kill'", con);
            Int32 killuser = (Int32)cmdFirstConnect.ExecuteScalar();
            cmdFirstConnect.ExecuteNonQuery();
            con.Close();
            con.Open();
            cmdFirstConnect = new SqlCommand("SELECT COUNT(*) FROM [User]  WHERE email='" + email + "'and password='" + password + "'", con);
            Int32 userControl = (Int32)cmdFirstConnect.ExecuteScalar();
            cmdFirstConnect.ExecuteNonQuery();
            con.Close();
            con.Open();
            cmdFirstConnect = new SqlCommand("SELECT COUNT(*) FROM Item INNER JOIN [User] ON Item.userid=[User].UserId  WHERE email='" + email + "'and password='" + password + "' and Mac='" + MacAddress + "'and Item.accessStatus='Kill'", con);
            Int32 accessStatus = (Int32)cmdFirstConnect.ExecuteScalar();
            cmdFirstConnect.ExecuteNonQuery();
            cmdFirstConnect.Connection.Close();
            con.Close();
            if ((userControl == 1) && (killuser == 1))
            {
                con.Open();
                cmdKillUser = new SqlCommand("DELETE FROM [User] WHERE email='" + email + "' ", con);
                cmdKillUser.ExecuteNonQuery();
                cmdKillUser.Connection.Close();
                con.Close();
                answer = "User Deleted From Table";
                
            }
            else if (userControl != 1)
            {
                answer = "Wrong Entry.Please Entry Again";
            }
            else if ((userControl == 1) && (killuser != 1))
            {
                if (accessStatus == 1)
                {
                    KillDevice(MacAddress);
                    answer = "Your Device Was Deleted from Table";
                }
                else
                {
                    answer = "Hi.Client";
                }
            }

            con.Close();
            return answer;
           
        }
        [WebMethod]
        public  bool still_alive()
        {
            return true;
        }  
        [WebMethod]
        public  bool Change_Password(string email, string oldpassword, string newpassword)
        {
            bool change_password=false;
            SqlCommand cmdChangePassword;
            con.Open();
            cmdChangePassword = new SqlCommand("UPDATE [User] SET password=@newpassword WHERE email='" + email + "' and password='" + oldpassword + "'", con);
            cmdChangePassword.Parameters.AddWithValue("@email", email);
            cmdChangePassword.Parameters.AddWithValue("@oldpassword", oldpassword);
            cmdChangePassword.Parameters.AddWithValue("@newpassword", newpassword);
            cmdChangePassword.ExecuteNonQuery();         
            con.Close();
            cmdChangePassword.Connection.Close();
            change_password = true;
            return change_password;
        }
        [WebMethod]
        public bool UpdateDevice(string Macadress, string status1, string status2, string status3)
        {
            bool update = false;
            SqlCommand updateDevice;
            con.Open();
            updateDevice = new SqlCommand("UPDATE [Item] SET status1=@status1,status2=@status2,status3=@status3  WHERE Mac='" + Macadress + "'",con);
            updateDevice.Parameters.AddWithValue("@status1", status1);
            updateDevice.Parameters.AddWithValue("@status2", status2);
            updateDevice.Parameters.AddWithValue("@status3", status3);
            updateDevice.Parameters.AddWithValue("@MacAddress", Macadress);
            updateDevice.ExecuteNonQuery();
            con.Close();
            update = true;
            return update;
        }

        [WebMethod]
        public string checkTopic(string mac)
        {
            SqlCommand cmddevicelist;
            string topic="";
            con.Open();

            cmddevicelist = new SqlCommand("SELECT topicname FROM [Topic] WHERE macadress='" + mac + "'", con);
            SqlDataReader reader = cmddevicelist.ExecuteReader();
            while (reader.Read())
            {
                topic = reader[0].ToString();
            }          
            con.Close();
            cmddevicelist.Connection.Close();

            return topic;
        }

        [WebMethod]
        public bool InsertDevice(string email,string password,string name, string type, string topicname)
        {
            bool insert = false;
            SqlCommand cmddevicelist;
            int id = 0;
            con.Open();

            cmddevicelist = new SqlCommand("SELECT UserId FROM [User] WHERE email='" + email + "' and password='" + password + "'", con);
            SqlDataReader reader = cmddevicelist.ExecuteReader();
            while (reader.Read())
            {
                id = Convert.ToInt32(reader[0].ToString());
            }
            con.Close();
            cmddevicelist.Connection.Close();


          
            SqlCommand cmdInsertDevice;
            con.Open();
            cmdInsertDevice = new SqlCommand("INSERT INTO [Device](Name,Type,topicname,userid) VALUES (@name,@type,@topicname,@id)", con);

            cmdInsertDevice.Parameters.Add("@name", name);
            cmdInsertDevice.Parameters.Add("@type", type);
            cmdInsertDevice.Parameters.Add("@id", id);
            cmdInsertDevice.Parameters.Add("@topicname", topicname);

            cmdInsertDevice.ExecuteNonQuery();
            cmdInsertDevice.Connection.Close();
            con.Close();
            insert = true;
            return insert;

        }


    }
}
