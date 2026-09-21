import SwiftUI
import Shared

func safeImageURL(_ rawUrl: String?) -> URL? {
    guard let rawUrl = rawUrl, !rawUrl.isEmpty else { return nil }
    let secureUrl = rawUrl.replacingOccurrences(of: "http://", with: "https://")
    return URL(string: secureUrl)
}

struct BookCardView: View {
    let book: Book

    var body: some View {
        HStack(spacing: 16) {
            AsyncImage(url: safeImageURL(book.coverUrl)) { image in
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
            .frame(width: 64, height: 96)
            .cornerRadius(8)
            .shadow(radius: 2)

            VStack(alignment: .leading, spacing: 4) {
                Text(book.title)
                    .font(.headline)
                    .foregroundColor(.deepInk)
                    .lineLimit(2)

                if let pubYear = book.firstPublishYear?.intValue {
                    Text("Published \(pubYear)")
                        .font(.subheadline)
                        .foregroundColor(.warmGrey)
                }

                if let progress = book.readingProgress {
                    Spacer().frame(height: 4)
                    Text("Page \(progress.currentPage) / \(book.pageCount)")
                        .font(.caption)
                        .foregroundColor(.warmGrey)

                    ProgressBarView(progress: Double(progress.percentage) / 100.0)
                }
            }
            Spacer()
        }
        .padding(12)
        .background(Color.white)
        .cornerRadius(12)
        .shadow(color: Color.black.opacity(0.05), radius: 4, x: 0, y: 2)
    }
}

struct VerticalBookCardView: View {
    let book: Book

    var body: some View {
        VStack(alignment: .leading, spacing: 6) {
            AsyncImage(url: safeImageURL(book.coverUrl)) { image in
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
            .frame(width: 130, height: 185)
            .cornerRadius(12)
            .shadow(radius: 3)

            Text(book.title)
                .font(.subheadline)
                .fontWeight(.semibold)
                .foregroundColor(.deepInk)
                .lineLimit(2)

            if let pubYear = book.firstPublishYear?.intValue {
                Text("\(pubYear)")
                    .font(.caption)
                    .foregroundColor(.warmGrey)
            }
        }
        .frame(width: 130)
    }
}
