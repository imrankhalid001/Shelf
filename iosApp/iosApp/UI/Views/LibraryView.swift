import SwiftUI
import Shared

struct LibraryView: View {
    @State private var books: [Book] = []

    var body: some View {
        NavigationStack {
            VStack {
                if books.isEmpty {
                    EmptyStateView(
                        title: "Your Library is Empty",
                        subtitle: "Search for books and save them to build your personal shelf."
                    )
                } else {
                    ScrollView {
                        LazyVStack(spacing: 12) {
                            ForEach(books, id: \.id) { book in
                                NavigationLink(destination: BookDetailsView(bookId: book.id)) {
                                    BookCardView(book: book)
                                }
                            }
                        }
                        .padding(16)
                    }
                }
            }
            .background(Color.paperLight)
            .navigationTitle("My Library")
            .navigationBarTitleDisplayMode(.inline)
            .task {
                loadLibrary()
            }
        }
    }

    private func loadLibrary() {
        Task {
            let bridge = KMPBridge.shared
            try? await bridge.getLibraryUseCase.observeAll().collectValues { bookList in
                if let list = bookList as? [Book] {
                    DispatchQueue.main.async {
                        self.books = list
                    }
                }
            }
        }
    }
}
