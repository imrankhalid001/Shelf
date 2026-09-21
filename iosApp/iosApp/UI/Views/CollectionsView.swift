import SwiftUI
import Shared

struct CollectionsView: View {
    @State private var collections: [Collection] = []
    @State private var showCreateSheet = false
    @State private var newName = ""
    @State private var newDescription = ""

    var body: some View {
        NavigationStack {
            VStack {
                if collections.isEmpty {
                    EmptyStateView(
                        title: "No Collections Yet",
                        subtitle: "Tap + to create custom book shelves like 'Productivity' or 'Fiction'."
                    )
                } else {
                    ScrollView {
                        LazyVStack(spacing: 12) {
                            ForEach(collections, id: \.id) { collection in
                                HStack(spacing: 16) {
                                    Image(systemName: "bookmark.fill")
                                        .foregroundColor(.amberAccent)
                                        .font(.title3)

                                    VStack(alignment: .leading, spacing: 4) {
                                        Text(collection.name)
                                            .font(.headline)
                                            .foregroundColor(.deepInk)

                                        if let desc = collection.description_ {
                                            Text(desc)
                                                .font(.subheadline)
                                                .foregroundColor(.warmGrey)
                                        }
                                    }
                                    Spacer()
                                }
                                .padding(16)
                                .background(Color.white)
                                .cornerRadius(12)
                                .overlay(
                                    RoundedRectangle(cornerRadius: 12)
                                        .stroke(Color.gray.opacity(0.3), lineWidth: 1)
                                )
                            }
                        }
                        .padding(16)
                    }
                }
            }
            .background(Color.paperLight)
            .navigationTitle("Collections")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button(action: { showCreateSheet = true }) {
                        Image(systemName: "plus")
                            .font(.title3)
                            .foregroundColor(.amberAccent)
                    }
                }
            }
            .sheet(isPresented: $showCreateSheet) {
                NavigationStack {
                    Form {
                        Section(header: Text("Collection Details")) {
                            TextField("Collection Name", text: $newName)
                            TextField("Description (Optional)", text: $newDescription)
                        }
                    }
                    .navigationTitle("New Collection")
                    .navigationBarTitleDisplayMode(.inline)
                    .toolbar {
                        ToolbarItem(placement: .cancellationAction) {
                            Button("Cancel") { showCreateSheet = false }
                        }
                        ToolbarItem(placement: .confirmationAction) {
                            Button("Create") {
                                createCollection()
                                showCreateSheet = false
                            }
                            .disabled(newName.trimmingCharacters(in: .whitespaces).isEmpty)
                        }
                    }
                }
            }
            .task {
                loadCollections()
            }
        }
    }

    private func loadCollections() {
        Task {
            let bridge = KMPBridge.shared
            try? await bridge.getCollectionsUseCase.invoke().collectValues { colList in
                if let list = colList as? [Collection] {
                    DispatchQueue.main.async {
                        self.collections = list
                    }
                }
            }
        }
    }

    private func createCollection() {
        Task {
            let bridge = KMPBridge.shared
            let desc = newDescription.trimmingCharacters(in: .whitespaces).isEmpty ? nil : newDescription
            _ = try? await bridge.createCollectionUseCase.invoke(name: newName, description: desc)
            newName = ""
            newDescription = ""
        }
    }
}
