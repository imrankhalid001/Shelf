import SwiftUI
import Shared

struct HomeView: View {
    @State private var currentlyReading: Book? = nil
    @State private var recommended: [Book] = []
    @State private var libraryBooks: [Book] = []
    @State private var streakDays: Int32 = 0
    @State private var totalPages: Int32 = 0
    @State private var totalMins: Int64 = 0

    var body: some View {
        NavigationStack {
            ScrollView {
                VStack(alignment: .leading, spacing: 20) {
                    // Welcome Header & Daily Streak Badge Card
                    HStack {
                        VStack(alignment: .leading, spacing: 4) {
                            Text("Welcome to Shelf 👋")
                                .font(.title3)
                                .fontWeight(.bold)
                                .foregroundColor(.deepInk)
                            Text("Your personal reading space")
                                .font(.subheadline)
                                .foregroundColor(.warmGrey)
                        }
                        Spacer()
                        Text("🔥 \(streakDays) Days")
                            .font(.caption)
                            .fontWeight(.bold)
                            .foregroundColor(.amberAccent)
                            .padding(.horizontal, 12)
                            .padding(.vertical, 6)
                            .background(Color.amberAccent.opacity(0.15))
                            .cornerRadius(16)
                    }
                    .padding(16)
                    .background(Color.amberAccent.opacity(0.1))
                    .cornerRadius(16)

                    // Continue Reading Section
                    if let book = currentlyReading {
                        VStack(alignment: .leading, spacing: 8) {
                            Text("Continue Reading")
                                .font(.headline)
                                .foregroundColor(.deepInk)

                            NavigationLink(destination: BookDetailsView(bookId: book.id)) {
                                BookCardView(book: book)
                            }
                        }
                    }

                    // Recommended for You Section
                    if !recommended.isEmpty {
                        VStack(alignment: .leading, spacing: 12) {
                            Text("Recommended for You")
                                .font(.headline)
                                .foregroundColor(.deepInk)

                            ScrollView(.horizontal, showsIndicators: false) {
                                HStack(spacing: 12) {
                                    ForEach(recommended, id: \.id) { book in
                                        NavigationLink(destination: BookDetailsView(bookId: book.id)) {
                                            VerticalBookCardView(book: book)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Saved Library Books Section
                    if !libraryBooks.isEmpty {
                        VStack(alignment: .leading, spacing: 12) {
                            Text("Saved in Your Library")
                                .font(.headline)
                                .foregroundColor(.deepInk)

                            ScrollView(.horizontal, showsIndicators: false) {
                                HStack(spacing: 12) {
                                    ForEach(libraryBooks, id: \.id) { book in
                                        NavigationLink(destination: BookDetailsView(bookId: book.id)) {
                                            VerticalBookCardView(book: book)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Quick Stats Section
                    VStack(alignment: .leading, spacing: 12) {
                        Text("Reading Intelligence")
                            .font(.headline)
                            .foregroundColor(.deepInk)

                        HStack(spacing: 12) {
                            VStack(alignment: .leading, spacing: 4) {
                                Text("Pages Read")
                                    .font(.caption)
                                    .foregroundColor(.warmGrey)
                                Text("\(totalPages)")
                                    .font(.title2)
                                    .fontWeight(.bold)
                                    .foregroundColor(.deepInk)
                            }
                            .padding(14)
                            .frame(maxWidth: .infinity, alignment: .leading)
                            .background(Color.white)
                            .cornerRadius(12)
                            .shadow(color: Color.black.opacity(0.05), radius: 3)

                            VStack(alignment: .leading, spacing: 4) {
                                Text("Reading Time")
                                    .font(.caption)
                                    .foregroundColor(.warmGrey)
                                Text("\(totalMins) mins")
                                    .font(.title2)
                                    .fontWeight(.bold)
                                    .foregroundColor(.deepInk)
                            }
                            .padding(14)
                            .frame(maxWidth: .infinity, alignment: .leading)
                            .background(Color.white)
                            .cornerRadius(12)
                            .shadow(color: Color.black.opacity(0.05), radius: 3)
                        }
                    }

                    if currentlyReading == nil && libraryBooks.isEmpty {
                        EmptyStateView(
                            title: "Start Building Your Library",
                            subtitle: "Search for your favorite books to add them to your shelf and track daily reading progress."
                        )
                    }
                }
                .padding(16)
            }
            .background(Color.paperLight)
            .navigationTitle("Shelf")
            .navigationBarTitleDisplayMode(.inline)
            .task {
                loadData()
            }
        }
    }

    private func loadData() {
        // Task 1: Library books & currently reading
        Task {
            try? await KMPBridge.shared.bookRepository.observeLibrary().collectValues { books in
                if let bookList = books as? [Book] {
                    DispatchQueue.main.async {
                        self.libraryBooks = bookList
                        self.currentlyReading = bookList.first(where: { $0.readingProgress?.status == .reading })
                    }
                }
            }
        }

        // Task 2: Reading Statistics
        Task {
            try? await KMPBridge.shared.statsRepository.observeStatistics().collectValues { stats in
                if let statsObj = stats as? ReadingStatistics {
                    DispatchQueue.main.async {
                        self.streakDays = statsObj.currentStreakDays
                        self.totalPages = statsObj.totalPagesRead
                        self.totalMins = statsObj.totalReadingTimeMinutes
                    }
                }
            }
        }

        // Task 3: Recommendations
        Task {
            try? await KMPBridge.shared.getRecommendationsUseCase.invoke().collectValues { recs in
                if let recList = recs as? [Book] {
                    DispatchQueue.main.async {
                        self.recommended = recList
                    }
                }
            }
        }
    }
}
