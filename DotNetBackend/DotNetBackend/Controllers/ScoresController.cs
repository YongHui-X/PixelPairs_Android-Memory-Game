using DotNetBackend.Data;
using DotNetBackend.Models;
using Microsoft.AspNetCore.Mvc;

namespace DotNetBackend.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class ScoresController : Controller
    {
        [HttpPost("submit")]
        public ActionResult<ScoreSubmissionResponse> SubmitScore([FromBody] ScoreSubmissionRequest request)
        {
            try
            {
                if (string.IsNullOrEmpty(request.UserName))
                {
                    return BadRequest(new ScoreSubmissionResponse
                    {
                        Success = false,
                        Message = "UserName is required"
                    });
                }
                if (request.Score <= 0)
                {
                    return BadRequest(new ScoreSubmissionResponse
                    {
                        Success = false,
                        Message = "Score must be greater than 0"
                    });
                }

                // Save the score 
                ScoresDAO.SavePlayerScore(request.UserName, request.Score);

                // Get updated leaderboare
                var leaderboard = ScoresDAO.GetTopFiveScores();

                // Find User's rank
                int? rank = null;
                bool isTopFive = false;

                for (int i = 0; i< leaderboard.Count; i++)
                {
                    if (leaderboard[i].Username == request.UserName)
                    {
                        rank = i + 1;
                        isTopFive = true;
                        break;
                    }
                }
                return Ok(new ScoreSubmissionResponse
                {
                    Success = true,
                    Message = "Score submitted successfully",
                    Rank = rank,
                    IsTopFive = isTopFive
                });
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error submitting score: {ex.Message}");

                return StatusCode(500, new ScoreSubmissionResponse
                {
                    Success = false,
                    Message = $"Error submitting score: {ex.Message}"
                });
            }
        }

        [HttpGet("leaderboard")]
        public ActionResult<LeaderboardResponse> GetLeaderboard()
        {
            try
            {
                //Get top 5
                var topScores = ScoresDAO.GetTopFiveScores();

                // Convert to DTO with rank and time
                var leaderboardDto = topScores.Select((entry, index) => new LeaderboardEntryDto
                {
                    Rank = index + 1,
                    Username = entry.Username,
                    Score = entry.Score,
                    FormattedTime = FormatTime(entry.Score)
                }).ToList();

                return Ok(new LeaderboardResponse
                {
                    Success = true,
                    Message = "Leaderboard retrieved successfully",
                    Leaderboard = leaderboardDto
                });
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error getting leaderboard: {ex.Message}");

                return StatusCode(500, new LeaderboardResponse
                {
                    Success = false,
                    Message = $"Error retrieving leaderboard: {ex.Message}",
                    Leaderboard = new List<LeaderboardEntryDto>()
                });
            }
        }
        [HttpGet("user/{username}")]
        public ActionResult<LeaderboardEntry> GetUserBestScore(string username)
        {
            try
            {
                var leaderboard = ScoresDAO.GetTopFiveScores();
                var userEntry = leaderboard.FirstOrDefault(e => e.Username == username);

                if (userEntry == null)
                {
                    return NotFound(new
                    {
                        success = false,
                        message = $"No scores found for user: {username}"
                    });
                }

                return Ok(new
                {
                    success = true,
                    username = userEntry.Username,
                    score = userEntry.Score,
                    formattedTime = FormatTime(userEntry.Score)
                });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new
                {
                    success = false,
                    message = $"Error: {ex.Message}"
                });
            }
        }

        private string FormatTime(long milliseconds)
        {
            TimeSpan timeSpan = TimeSpan.FromMilliseconds(milliseconds);
            return timeSpan.ToString(@"mm\:ss");
        }
    }
}
