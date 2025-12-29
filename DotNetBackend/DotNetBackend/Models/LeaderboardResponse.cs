namespace DotNetBackend.Models
{
    public class LeaderboardResponse
    {
        public bool Success { get; set; }
        public string Message { get; set; }
        public List<LeaderboardEntryDto> Leaderboard { get; set; }

    }

    public class LeaderboardEntryDto
    {
        public int Rank { get; set; }
        public string Username { get; set; }
        public long Score { get; set; }

        public string FormattedTime { get; set; }
    }
}
