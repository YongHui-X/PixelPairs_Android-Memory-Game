using System.Reflection.Metadata;
using DotNetBackend.Data;
using Microsoft.AspNetCore.Mvc;
using MySql.Data.MySqlClient;

namespace DotNetBackend.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class AuthController : ControllerBase
    {

        [HttpPost("login")]
        public IActionResult Login([FromBody] LoginRequest request)
        {
            using (MySqlConnection conn = new MySqlConnection(Constants.CONNECTION_STRING))
            {
                conn.Open();

                string query = @"SELECT u.Username, ud.UserType 
                                FROM Users u 
                                INNER JOIN UserDetails ud ON u.Username = ud.Username
                                WHERE u.Username = @username AND u.Password = @password";

                using (MySqlCommand cmd = new MySqlCommand(query, conn))
                {
                    cmd.Parameters.AddWithValue("@username", request.Username);
                    cmd.Parameters.AddWithValue("@password", request.Password);

                    using (MySqlDataReader reader = cmd.ExecuteReader())
                    {
                        if (reader.Read())
                        {
                            return Ok(new
                            {
                                success = true,
                                username = reader.GetString("Username"),
                                isPaid = reader.GetString("UserType") == "Paid"
                            });
                        }
                        
                        return Unauthorized(new
                        {
                            success = false,
                            message = "Invalid username or password"
                        });
                    }
                }
            }
        }
    }

    public class LoginRequest
    {
        public string Username { get; set; }
        public string Password { get; set; }
    }
}