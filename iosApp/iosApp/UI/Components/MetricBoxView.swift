import SwiftUI

struct MetricBoxView: View {
    let title: String
    let value: String
    let subtitle: String

    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text(title)
                .font(.caption)
                .foregroundColor(.warmGrey)
            Text(value)
                .font(.title2)
                .fontWeight(.bold)
                .foregroundColor(.deepInk)
            Text(subtitle)
                .font(.caption2)
                .foregroundColor(.warmGrey.opacity(0.8))
        }
        .padding(14)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(Color.white)
        .cornerRadius(12)
        .shadow(color: Color.black.opacity(0.05), radius: 3)
    }
}
