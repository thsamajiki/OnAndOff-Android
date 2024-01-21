# This file contains the fastlane.tools configuration
# You can find the documentation at https://docs.fastlane.tools
#
# For a list of all available actions, check out
#
#     https://docs.fastlane.tools/actions
#
# For a list of all available plugins, check out
#
#     https://docs.fastlane.tools/plugins/available-plugins
#

# Uncomment the line if you want fastlane to automatically update itself
# update_fastlane

# default_platform(:android)

# platform :android do
#   desc "Runs all the tests"
#   lane :test do
#     gradle(task: "test")
#   end
#
#   desc "Submit a new Beta Build to Crashlytics Beta"
#   lane :beta do
#     gradle(task: "clean assembleRelease")
#     crashlytics
#
#     # sh "your_script.sh"
#     # You can also use other beta testing services here
#   end
#
#   desc "Deploy a new version to the Google Play"
#   lane :deploy do
#     gradle(task: "clean assembleRelease")
#     upload_to_play_store
#   end
# end

default_platform(:android)

platform :android do
    desc "Submit a new Beta Build to Crashlytics Beta"
        lane :beta do
            gradle(task: "clean assembleRelease")
            crashlytics

            # sh "your_script.sh"
            # You can also use other beta testing services here
        end

#     # 모든 테스트 트랙에 출시 및 테스트 앱 배포
#     desc "deploy all track"
#         lane :deployAll do |options|
#         develop(options)
#         alpha(options)
#         release(options)
#         gitCommitAndPush()
#     end

    desc "Deploy a new version to the Google Play"
        lane :deploy do
            gradle(task: "clean assembleRelease")
            upload_to_play_store
        end

        desc "Lane for distribution"
            lane :stagingDeploy do

            gradle(task: "clean assembleDebug")

            firebase_app_distribution(
                app: "1:751634127071:android:13845ffecc2829b3e6f16a",
                groups: "OnandOff",
                release_notes_file: "fastlane/release-notes.txt",
                firebase_cli_token: "1//0ekFftxojojIjCgYIARAAGA4SNwF-L9Irpy5AVlHyCMjYinkMknpo1G9Xcdqr_74-n7uWhO92nkbg_PtItOEyoDjG90Gi99V9Efc",
                debug: false
            )
        end

    desc "Deploy a new version to the Google Play"
        lane :deploy do
            gradle(task: "clean assembleRelease")
            upload_to_play_store
        end

    desc "Deploy a new version to the Google Play (ProductionType)"
    lane :productionDeploy do
    gradle(task: "release")

    # 특정 폴더에 .aab 파일 찾고 나서 앞에 .. -> . 치환 처리
    filePath = Dir['../app/release/*.aab'].last
    artifactPath = filePath.sub("..",".")
    puts artifactPath

# # production 트랙 출시
# desc "deploy production"
# lane :release do
# gradle(task: "clean")
# # 빌드
# gradle(
#     task: "bundle",
#     build_type: "release",
#     print_command: true,
#     properties: {
#     # app signing
#     "android.injected.signing.store.file" => ENV['SIGNED_STORE_FILE'],
#     "android.injected.signing.store.password" => ENV['SIGNED_STORE_PASSWORD'],
#     "android.injected.signing.key.alias" => ENV['SIGNED_KEY_ALIAS'],
#     "android.injected.signing.key.password" => ENV['SIGNED_KEY_PASSWORD']
#     }
# )
    # 플레이스토어에 업로드
    upload_to_play_store(
        track: "production",
        json_key: ENV['GOOGLE_SERVICE_ACCOUNT_KEY'],
        aab: ENV['AAB_RELEASE_PATH'],
        in_app_update_priority: priority,
        skip_upload_metadata: true,
        skip_upload_images: true,
        skip_upload_screenshots: true,
        skip_upload_apk: true
        release_status: "completed"
    )
    end
end