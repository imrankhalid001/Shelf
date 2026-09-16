import SwiftUI

struct ProgressBarView: View {
    let progress: Double

    var body: some View {
        GeometryReader { geometry in
            ZStack(alignment: .leading) {
                Rectangle()
                    .fill(Color.gray.opacity(0.2))
                    .frame(height: 8)
                    .cornerRadius(4)

                Rectangle()
                    .fill(Color.amberAccent)
                    .frame(width: geometry.size.width * CGFloat(min(max(progress, 0.0), 1.0)), height: 8)
                    .cornerRadius(4)
            }
        }
        .frame(height: 8)
    }
}
