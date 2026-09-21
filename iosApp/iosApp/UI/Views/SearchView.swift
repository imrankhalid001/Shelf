import SwiftUI
import Shared

struct SearchView: View {
    @State private var query = ""
    @State private var searchResults: [Book] = []
    @State private var isLoading = false
    @State private var errorMessage: String? = nil

    var body: some View {
        NavigationStack {
            VStack(spacing: 16) {
                HStack {
                    Image(systemName: "magnifyingglass")
                        .foregroundColor(.warmGrey)
                    TextField("Search by title or author...", text: $query)
                        .autocapitalization(.none)
                        .disableAutocorrection(true)
                        .onChange(of: query) { newQuery in
                            performSearch(query: newQuery)
                        }
                    if !query.isEmpty {
                        Button(action: { query = "" }) {
                            Image(systemName: "xmark.circle.fill")
                                .foregroundColor(.warmGrey)
                        }
                    }
                }
                .padding(12)
                .background(Color.white)
                .cornerRadius(12)
                .padding(.horizontal, 16)
                .shadow(color: Color.black.opacity(0.05), radius: 3)

                if isLoading {
                    ProgressView("Searching Open Library...")
                        .padding(.top, 32)
                    Spacer()
                } else if let error = errorMessage {
                    EmptyStateView(
                        title: "Network Error",
                        subtitle: error
                    )
                    Spacer()
                } else if searchResults.isEmpty {
                    EmptyStateView(
                        title: query.isEmpty ? "Search Open Library" : "No Books Found",
                        subtitle: query.isEmpty ? "Type a book title above to explore." : "Try searching for a different keyword."
                    )
                    Spacer()
                } else {
                    ScrollView {
                        LazyVStack(spacing: 12) {
                            ForEach(searchResults, id: \.id) { book in
                                NavigationLink(destination: BookDetailsView(bookId: book.id)) {
                                    BookCardView(book: book)
                                }
                            }
                        }
                        .padding(.horizontal, 16)
                    }
                }
            }
            .background(Color.paperLight)
            .navigationTitle("Search Books")
            .navigationBarTitleDisplayMode(.inline)
        }
    }

    private func performSearch(query: String) {
        guard !query.trimmingCharacters(in: .whitespaces).isEmpty else {
            searchResults = []
            isLoading = false
            errorMessage = nil
            return
        }

        isLoading = true
        errorMessage = nil

        Task {
            let bridge = KMPBridge.shared
            try? await bridge.searchBooksUseCase.invoke(query: query, page: 1).collectValues { result in
                DispatchQueue.main.async {
                    self.isLoading = false
                    if let success = result as? AppResultSuccess<NSArray>, let docs = success.data as? [Book] {
                        self.searchResults = docs
                    } else if let _ = result as? AppResultError {
                        self.errorMessage = "Unable to connect to Open Library."
                    }
                }
            }
        }
    }
}
