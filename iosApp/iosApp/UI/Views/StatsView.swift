import SwiftUI
import Shared

struct StatsView: View {
    @State private var streakDays: Int32 = 0
    @State private var totalPages: Int32 = 0
    @State private var totalTimeMins: Int64 = 0
    @State private var finishedBooks: Int32 = 0
    @State private var goalTarget: Int32 = 20

    var body: some View {
        NavigationStack {
            ScrollView {
                VStack(spacing: 16) {
                    // Streak Hero Banner
                    HStack {
                        VStack(alignment: .leading, spacing: 4) {
                            Text("🔥 \(streakDays) Day Streak")
                                .font(.title2)
                                .fontWeight(.bold)
                                .foregroundColor(.amberAccent)
                            Text("Keep reading every day to build your habit!")
                                .font(.subheadline)
                                .foregroundColor(.warmGrey)
                        }
                        Spacer()
                        Text("Avid Reader 📚")
                            .font(.caption)
                            .fontWeight(.semibold)
                            .padding(.horizontal, 10)
                            .padding(.vertical, 6)
                            .background(Color.white)
                            .cornerRadius(12)
                    }
                    .padding(16)
                    .background(Color.amberAccent.opacity(0.12))
                    .cornerRadius(16)

                    // 2x2 Metrics Grid
                    let pace = StringUtils.shared.formatReadingPace(pagesRead: totalPages, durationMinutes: totalTimeMins)
                    LazyVGrid(columns: [GridItem(.flexible()), GridItem(.flexible())], spacing: 12) {
                        MetricBoxView(title: "Pages Read", value: "\(totalPages)", subtitle: "Total pages")
                        MetricBoxView(title: "Reading Time", value: "\(totalTimeMins) mins", subtitle: "Time spent")
                        MetricBoxView(title: "Books Completed", value: "\(finishedBooks)", subtitle: "Finished books")
                        MetricBoxView(title: "Reading Speed", value: "\(pace) p/hr", subtitle: "Average pace")
                    }

                    // 2026 Goal Card
                    let completed = finishedBooks
                    let percentage = goalTarget > 0 ? Double(completed) / Double(goalTarget) : 0.0
                    VStack(alignment: .leading, spacing: 12) {
                        Text("2026 Reading Goal Target")
                            .font(.headline)
                            .foregroundColor(.deepInk)

                        Text("\(completed) / \(goalTarget) books completed (\(Int(percentage * 100))%)")
                            .font(.subheadline)
                            .foregroundColor(.warmGrey)

                        ProgressBarView(progress: percentage)

                        HStack(spacing: 12) {
                            Spacer()
                            Button(action: { updateGoal(target: goalTarget - 5) }) {
                                Text("-5 Goal")
                                    .font(.subheadline)
                                    .fontWeight(.bold)
                                    .foregroundColor(.deepInk)
                                    .padding(.horizontal, 14)
                                    .padding(.vertical, 8)
                                    .background(Color.gray.opacity(0.15))
                                    .cornerRadius(8)
                            }
                            .disabled(goalTarget <= 5)

                            Button(action: { updateGoal(target: goalTarget + 5) }) {
                                Text("+5 Goal")
                                    .font(.subheadline)
                                    .fontWeight(.bold)
                                    .foregroundColor(.white)
                                    .padding(.horizontal, 14)
                                    .padding(.vertical, 8)
                                    .background(Color.amberAccent)
                                    .cornerRadius(8)
                            }
                        }
                    }
                    .padding(16)
                    .background(Color.white)
                    .cornerRadius(12)
                    .shadow(color: Color.black.opacity(0.05), radius: 3)

                    // Monthly Distribution Chart Card
                    VStack(alignment: .leading, spacing: 12) {
                        Text("Monthly Reading Distribution")
                            .font(.headline)
                            .foregroundColor(.deepInk)

                        let months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"]
                        HStack(alignment: .bottom, spacing: 8) {
                            ForEach(Array(months.enumerated()), id: \.offset) { index, month in
                                VStack {
                                    Spacer()
                                    Rectangle()
                                        .fill(index == 2 && totalPages > 0 ? Color.amberAccent : Color.gray.opacity(0.2))
                                        .frame(height: index == 2 && totalPages > 0 ? 70 : 15)
                                        .cornerRadius(4)
                                    Text(month)
                                        .font(.caption2)
                                        .foregroundColor(.warmGrey)
                                }
                                .frame(maxWidth: .infinity)
                            }
                        }
                        .frame(height: 100)
                    }
                    .padding(16)
                    .background(Color.white)
                    .cornerRadius(12)
                    .shadow(color: Color.black.opacity(0.05), radius: 3)
                }
                .padding(16)
            }
            .background(Color.paperLight)
            .navigationTitle("Reading Intelligence")
            .navigationBarTitleDisplayMode(.inline)
            .task {
                loadStats()
            }
        }
    }

    private func loadStats() {
        Task {
            try? await KMPBridge.shared.statsRepository.observeStatistics().collectValues { stats in
                if let statsObj = stats as? ReadingStatistics {
                    DispatchQueue.main.async {
                        self.streakDays = statsObj.currentStreakDays
                        self.totalPages = statsObj.totalPagesRead
                        self.totalTimeMins = statsObj.totalReadingTimeMinutes
                        self.finishedBooks = statsObj.totalBooksReadThisYear
                    }
                }
            }
        }

        Task {
            let currentYear = DateUtils.shared.currentYear()
            try? await KMPBridge.shared.statsRepository.observeGoal(year: currentYear).collectValues { g in
                if let goalObj = g as? ReadingGoal {
                    DispatchQueue.main.async {
                        self.goalTarget = goalObj.targetBooks
                    }
                }
            }
        }
    }

    private func updateGoal(target: Int32) {
        let newTarget = max(1, target)
        self.goalTarget = newTarget
        Task {
            let currentYear = DateUtils.shared.currentYear()
            _ = try? await KMPBridge.shared.setReadingGoalUseCase.invoke(year: currentYear, targetBooks: newTarget)
        }
    }
}
