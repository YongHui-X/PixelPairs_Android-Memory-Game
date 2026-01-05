using DotNetBackend.Models;
using MySqlConnector;
namespace DotNetBackend.Data
{
    public class ScoresDAO
    {

        public static void SavePlayerScore(string username, long score)
        {
            using (MySqlConnection conn = new MySqlConnection(Constants.CONNECTION_STRING))
            {
                conn.Open();

                string getUserIdQuery = "SELECT Id FROM UserDetails WHERE Username = @username";
                long UserDetailId;

                using (MySqlCommand cmd = new MySqlCommand(getUserIdQuery, conn))
                {
                    cmd.Parameters.AddWithValue("@username", username);
                    UserDetailId = (long)cmd.ExecuteScalar();
                }

                string insertScoreQuery = "INSERT INTO Scores(UserDetailId, Score) VALUES (@uid, @score)";

                using (MySqlCommand cmd = new MySqlCommand(insertScoreQuery, conn))
                {
                    cmd.Parameters.AddWithValue("@uid", UserDetailId);
                    cmd.Parameters.AddWithValue("@score", score);
                    cmd.ExecuteNonQuery();
                }
            }
        }

        public static List<LeaderboardEntry> GetTopFiveScores()
        {
            var leaderboard = new List<LeaderboardEntry>();

            using (MySqlConnection conn = new MySqlConnection(Constants.CONNECTION_STRING))
            {
                conn.Open();
                string sql = @"SELECT UD.Username,
                              MIN(S.Score) AS Score
                              FROM UserDetails UD
                              JOIN Scores S ON UD.Id = S.UserDetailId
                              GROUP BY UD.Username
                              ORDER BY Score
                              LIMIT 5";
                MySqlCommand cmd = new MySqlCommand(sql, conn);
                MySqlDataReader reader = cmd.ExecuteReader();

                while (reader.Read())
                {
                    leaderboard.Add(new LeaderboardEntry()
                    {
                        Username = reader.GetString("Username"),
                        Score = reader.GetInt64("Score")
                    });
                }
            }
            return leaderboard;
        }
    }
}
