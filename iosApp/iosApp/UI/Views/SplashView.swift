import SwiftUI

struct SplashView: View {
    @State private var isActive = false
    @State private var scaleAmount: CGFloat = 0.8
    @State private var opacityAmount: Double = 0.0

    var body: some View {
        if isActive {
            ContentView()
        } else {
            ZStack {
                Color.deepInk
                    .ignoresSafeArea()

                VStack(spacing: 20) {
                    // Logo Canvas
                    ZStack {
                        RoundedRectangle(cornerRadius: 24)
                            .fill(Color(red: 26/255.0, green: 25/255.0, blue: 24/255.0))
                            .frame(width: 120, height: 120)
                            .shadow(color: Color.black.opacity(0.3), radius: 8, x: 0, y: 4)

                        VStack(spacing: 8) {
                            HStack(spacing: 2) {
                                // Left Page
                                Rectangle()
                                    .fill(Color(red: 253/255.0, green: 251/255.0, blue: 247/255.0))
                                    .frame(width: 32, height: 44)
                                    .cornerRadius(4, corners: [.topLeft, .bottomLeft])

                                // Right Page
                                Rectangle()
                                    .fill(Color(red: 245/255.0, green: 243/255.0, blue: 239/255.0))
                                    .frame(width: 32, height: 44)
                                    .cornerRadius(4, corners: [.topRight, .bottomRight])
                            }
                            .overlay(
                                // Ribbon
                                Rectangle()
                                    .fill(Color.amberAccent)
                                    .frame(width: 8, height: 24)
                                    .offset(x: 2, y: -10),
                                alignment: .top
                            )

                            // Shelf Base Line
                            Rectangle()
                                .fill(Color.amberAccent)
                                .frame(width: 80, height: 4)
                                .cornerRadius(2)
                        }
                    }
                    .scaleEffect(scaleAmount)
                    .opacity(opacityAmount)

                    VStack(spacing: 6) {
                        Text("Shelf")
                            .font(.system(size: 32, weight: .bold, design: .serif))
                            .foregroundColor(.white)

                        Text("Personal Book Intelligence")
                            .font(.subheadline)
                            .foregroundColor(.warmGrey)
                    }
                    .opacity(opacityAmount)
                }
            }
            .onAppear {
                withAnimation(.easeOut(duration: 1.0)) {
                    self.scaleAmount = 1.0
                    self.opacityAmount = 1.0
                }
                DispatchQueue.main.asyncAfter(deadline: .now() + 1.6) {
                    withAnimation(.easeInOut(duration: 0.4)) {
                        self.isActive = true
                    }
                }
            }
        }
    }
}

extension View {
    func cornerRadius(_ radius: CGFloat, corners: UIRectCorner) -> some View {
        clipShape(RoundedCornerShape(radius: radius, corners: corners))
    }
}

struct RoundedCornerShape: Shape {
    var radius: CGFloat = .infinity
    var corners: UIRectCorner = .allCorners

    func path(in rect: CGRect) -> Path {
        let path = UIBezierPath(
            roundedRect: rect,
            byRoundingCorners: corners,
            cornerRadii: CGSize(width: radius, height: radius)
        )
        return Path(path.cgPath)
    }
}
