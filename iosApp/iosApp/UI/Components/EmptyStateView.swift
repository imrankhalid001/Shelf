import SwiftUI

struct EmptyStateView: View {
    let title: String
    let subtitle: String

    var body: some View {
        VStack(spacing: 8) {
            Text(title)
                .font(.headline)
                .foregroundColor(.deepInk)
            Text(subtitle)
                .font(.subheadline)
                .foregroundColor(.warmGrey)
                .multilineTextAlignment(.center)
        }
        .padding(32)
    }
}
