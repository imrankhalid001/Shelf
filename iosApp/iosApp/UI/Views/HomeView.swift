import SwiftUI

struct HomeView: View {
    var body: some View {
        NavigationStack {
            VStack {
                Text("Shelf")
                    .font(.largeTitle)
                    .bold()
                EmptyStateView(
                    title: "Your Personal Reading Space",
                    subtitle: "Discover, track, and organize your books offline."
                )
            }
            .navigationTitle("Home")
        }
    }
}

#Preview {
    HomeView()
}
