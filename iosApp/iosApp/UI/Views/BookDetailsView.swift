import SwiftUI
import Shared

struct BookDetailsView: View {
    let bookId: String
    @State private var book: Book? = nil
    @State private var sliderValue: Double = 0.0
    @State private var isLoading = true

    var body: some View {
        ScrollView {
            VStack(spacing: 20) {
                if let loadedBook = book {
                    // Book Hero Card
                    VStack(spacing: 12) {
                        AsyncImage(url: safeImageURL(loadedBook.coverUrl)) { image in
                            image
                                .resizable()
                                .aspectRatio(contentMode: .fill)
                        } placeholder: {
                            Rectangle()
                                .fill(Color.gray.opacity(0.15))
                                .overlay(
                                    Image(systemName: "book.fill")
                                        .foregroundColor(.warmGrey)
                                )
                        }
                        .frame(width: 140, height: 210)
                        .cornerRadius(12)
                        .shadow(radius: 4)

                        Text(loadedBook.title)
                            .font(.title2)
                            .fontWeight(.bold)
                            .foregroundColor(.deepInk)
                            .multilineTextAlignment(.center)

                        if let pubYear = loadedBook.firstPublishYear?.intValue {
                            Text("First published in \(pubYear)")
                                .font(.subheadline)
                                .foregroundColor(.warmGrey)
                        }

                        Button(action: { saveBook(loadedBook) }) {
                            Text("Save to Library")
                                .font(.headline)
                                .foregroundColor(.white)
                                .frame(maxWidth: .infinity)
                                .padding(.vertical, 12)
                                .background(Color.amberAccent)
                                .cornerRadius(12)
                        }

                        Button(action: { openOpenLibraryWebReader(workId: loadedBook.workId) }) {
                            HStack {
                                Image(systemName: "book.pages")
                                Text("Read Free on Open Library 📖")
                            }
                            .font(.subheadline)
                            .fontWeight(.medium)
                            .foregroundColor(.deepInk)
                            .frame(maxWidth: .infinity)
                            .padding(.vertical, 12)
                            .overlay(
                                RoundedRectangle(cornerRadius: 12)
                                    .stroke(Color.warmGrey.opacity(0.5), lineWidth: 1)
                            )
                        }
                    }
                    .padding(20)
                    .background(Color.white)
                    .cornerRadius(16)
                    .shadow(color: Color.black.opacity(0.05), radius: 4)

                    // Reading Progress Section
                    let totalPages = max(loadedBook.pageCount, 100)
                    let currentPg = Int(sliderValue)
                    let pct = totalPages > 0 ? Int((sliderValue / Double(totalPages)) * 100.0) : 0

                    VStack(alignment: .leading, spacing: 12) {
                        Text("Reading Progress")
                            .font(.headline)
                            .foregroundColor(.deepInk)

                        Text("Page \(currentPg) / \(totalPages) (\(pct)%)")
                            .font(.subheadline)
                            .foregroundColor(.warmGrey)

                        ProgressBarView(progress: sliderValue / Double(totalPages))

                        Slider(
                            value: $sliderValue,
                            in: 0...Double(totalPages),
                            step: 1.0,
                            onEditingChanged: { editing in
                                if !editing {
                                    updateProgress(bookId: loadedBook.id, page: Int32(sliderValue), total: totalPages)
                                }
                            }
                        )
                        .accentColor(.amberAccent)
                    }
                    .padding(16)
                    .background(Color.white)
                    .cornerRadius(12)
                    .shadow(color: Color.black.opacity(0.05), radius: 3)

                    // Reading Status Selector
                    VStack(alignment: .leading, spacing: 12) {
                        Text("Reading Status")
                            .font(.headline)
                            .foregroundColor(.deepInk)

                        ScrollView(.horizontal, showsIndicators: false) {
                            HStack(spacing: 8) {
                                StatusChipView(title: "To Read", status: .wantToRead, currentStatus: loadedBook.readingProgress?.status) {
                                    updateStatus(bookId: loadedBook.id, status: .wantToRead)
                                }
                                StatusChipView(title: "Reading", status: .reading, currentStatus: loadedBook.readingProgress?.status) {
                                    updateStatus(bookId: loadedBook.id, status: .reading)
                                }
                                StatusChipView(title: "Finished", status: .finished, currentStatus: loadedBook.readingProgress?.status) {
                                    updateStatus(bookId: loadedBook.id, status: .finished)
                                }
                                StatusChipView(title: "Paused", status: .paused, currentStatus: loadedBook.readingProgress?.status) {
                                    updateStatus(bookId: loadedBook.id, status: .paused)
                                }
                                StatusChipView(title: "Dropped", status: .dropped, currentStatus: loadedBook.readingProgress?.status) {
                                    updateStatus(bookId: loadedBook.id, status: .dropped)
                                }
                            }
                        }
                    }
                    .padding(16)
                    .background(Color.white)
                    .cornerRadius(12)
                    .shadow(color: Color.black.opacity(0.05), radius: 3)
                } else if isLoading {
                    ProgressView("Loading book details...")
                        .padding(.top, 40)
                } else {
                    EmptyStateView(
                        title: "Book Not Found",
                        subtitle: "Could not load details for this book."
                    )
                }
            }
            .padding(16)
        }
        .background(Color.paperLight)
        .navigationTitle("Book Details")
        .navigationBarTitleDisplayMode(.inline)
        .task {
            loadBookDetails()
        }
    }

    private func loadBookDetails() {
        Task {
            let bridge = KMPBridge.shared
            try? await bridge.getBookDetailsUseCase.invoke(bookId: bookId).collectValues { result in
                DispatchQueue.main.async {
                    self.isLoading = false
                    if let success = result as? AppResultSuccess<Book>, let b = success.data {
                        self.book = b
                        if let currentP = b.readingProgress {
                            self.sliderValue = Double(currentP.currentPage)
                        }
                    }
                }
            }
        }
    }

    private func saveBook(_ bookToSave: Book) {
        Task {
            _ = try? await KMPBridge.shared.bookRepository.saveBook(book: bookToSave)
        }
    }

    private func updateProgress(bookId: String, page: Int32, total: Int32) {
        Task {
            _ = try? await KMPBridge.shared.updateReadingProgressUseCase.invoke(bookId: bookId, currentPage: page, totalPages: total)
        }
    }

    private func updateStatus(bookId: String, status: ReadingStatus) {
        Task {
            _ = try? await KMPBridge.shared.bookRepository.updateReadingStatus(bookId: bookId, status: status)
        }
    }

    private func openOpenLibraryWebReader(workId: String) {
        if let url = URL(string: "https://openlibrary.org/works/\(workId)") {
            UIApplication.shared.open(url)
        }
    }
}

struct StatusChipView: View {
    let title: String
    let status: ReadingStatus
    let currentStatus: ReadingStatus?
    let action: () -> Void

    var isSelected: Bool {
        status == currentStatus
    }

    var body: some View {
        Button(action: action) {
            Text(title)
                .font(.subheadline)
                .fontWeight(isSelected ? .semibold : .regular)
                .padding(.horizontal, 14)
                .padding(.vertical, 8)
                .background(isSelected ? Color.amberAccent.opacity(0.15) : Color.gray.opacity(0.1))
                .foregroundColor(isSelected ? .amberAccent : .deepInk)
                .cornerRadius(20)
                .overlay(
                    RoundedRectangle(cornerRadius: 20)
                        .stroke(isSelected ? Color.amberAccent : Color.gray.opacity(0.3), lineWidth: 1)
                )
        }
    }
}
