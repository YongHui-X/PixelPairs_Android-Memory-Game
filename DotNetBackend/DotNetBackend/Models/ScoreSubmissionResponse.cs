namespace DotNetBackend.Models
{
    public class ScoreSubmissionResponse
    {
        public bool Success { get; set; }
        public string Message { get; set; }
        public int? Rank { get; set; }
        public bool IsTopFive { get; set; }
    }
}
